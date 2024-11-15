/*
* Copyright (C) 2014 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.nabu.libs.resources.zip;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import be.nabu.libs.resources.api.Archive;
import be.nabu.libs.resources.api.ReadableResource;
import be.nabu.libs.resources.api.Resource;
import be.nabu.libs.resources.api.ResourceContainer;
import be.nabu.libs.resources.api.LocatableResource;
import be.nabu.utils.io.IOUtils;

public class ZIPArchive implements Archive<Resource>, Closeable, LocatableResource {

	private Resource source;
	
	/**
	 * Maps the fully qualified parent name to the children it contains
	 */
	private Map<ResourceContainer<?>, List<Resource>> entries;
	private Map<String, ResourceContainer<?>> directories;
	
	protected List<Resource> getChildren(ResourceContainer<?> path) throws IOException {
		if (entries == null) {
			directories = new HashMap<String, ResourceContainer<?>>();
			entries = new HashMap<ResourceContainer<?>, List<Resource>>();
			
			entries.put(this, new ArrayList<Resource>());
			ZipInputStream input = new ZipInputStream(IOUtils.toInputStream(((ReadableResource) getSource()).getReadable()));
			try {
				ZipEntry entry = null;
				while ((entry = input.getNextEntry()) != null) {
					ResourceContainer<?> parent = getParent(entry.getName());
				
					if (!entries.containsKey(parent))
						entries.put(parent, new ArrayList<Resource>());

					// directories are auto-created
					if (entry.isDirectory()) {
						if (!entries.get(parent).contains(getDirectory(entry.getName())))
							entries.get(parent).add(getDirectory(entry.getName()));
					}
					else					
						entries.get(parent).add(new ZIPItem(parent, (ReadableResource) getSource(), entry));
				}
			}
			finally {
				input.close();
			}
		}
		return entries.get(path);
	}
	
	private ResourceContainer<?> getDirectory(String path) {
		// remove leading/trailing slashes
		path = path.replaceAll("^[/]*(.*?)[/]*$", "$1");
		// if it's empty or explicitly the root, you mean the archive itself
		if (path.isEmpty() || path.equals("/"))
			return this;
		// otherwise, if the directory you are looking for has not yet been created
		else if (!directories.containsKey(path)) {
			// add this directory to the map of directories
			directories.put(path, new ZIPDirectory(this, path));
			// get the parent
			ResourceContainer<?> parent = path.contains("/") ? getDirectory(path.replaceAll("(.*)/[^/]+$", "$1")) : this;
			// add it to the parent
			entries.get(parent).add(directories.get(path));
			// make sure to create a list for this element as well
			entries.put(directories.get(path), new ArrayList<Resource>());
//			directories.put(path, new ZIPDirectory(this, path));
		}
		return directories.get(path);
	}
	
	protected ResourceContainer<?> getParent(String path) {
		return getDirectory(path.replaceAll("^[/]*(.*?)[/]*$", "$1").replaceAll("^(.*?)[^/]+$", "$1").replaceAll("[/]*$", ""));
	}

	@Override
	public Resource getChild(String name) {
		for (Resource child : this) {
			if (child.getName().equals(name)) {
				return child;
			}
		}
		return null;
	}

	@Override
	public Iterator<Resource> iterator() {
		try {
			return getChildren(this).iterator();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Resource getSource() {
		return source;
	}

	@Override
	public void setSource(Resource source) {
		this.source = source;
	}

	@Override
	public String getContentType() {
		return source.getContentType();
	}

	@Override
	public String getName() {
		return source.getName();
	}

	@Override
	public ResourceContainer<?> getParent() {
		return source.getParent();
	}

	@Override
	public URI getUri() {
		return ((LocatableResource) source).getUri();
	}

	@Override
	public void close() throws IOException {
		// do nothing
	}
}
