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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import be.nabu.libs.resources.api.Resource;
import be.nabu.libs.resources.api.ResourceContainer;

public class ZIPDirectory implements ResourceContainer<Resource> {

	private String path;
	private ZIPArchive archive;
	
	public ZIPDirectory(ZIPArchive archive, String path) {
		this.archive = archive;
		this.path = path;
	}

	@Override
	public String getContentType() {
		return Resource.CONTENT_TYPE_DIRECTORY;
	}

	protected List<Resource> getChildren() throws IOException {
		return archive.getChildren(this);
	}

	@Override
	public String getName() {
		return path.replaceAll("^.*?([^/]+)$", "$1");
	}

	@Override
	public ResourceContainer<?> getParent() {
		return archive.getParent(path);
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
			return getChildren().iterator();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void close() throws IOException {
		archive.close();
	}
}
