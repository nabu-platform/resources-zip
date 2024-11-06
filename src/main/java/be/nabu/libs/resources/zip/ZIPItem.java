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
import java.net.URLConnection;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import be.nabu.libs.resources.api.FiniteResource;
import be.nabu.libs.resources.api.ReadableResource;
import be.nabu.libs.resources.api.ResourceContainer;
import be.nabu.libs.resources.api.TimestampedResource;
import be.nabu.utils.io.IOUtils;
import be.nabu.utils.io.api.ByteBuffer;
import be.nabu.utils.io.api.ReadableContainer;

public class ZIPItem implements ReadableResource, FiniteResource, TimestampedResource {

	private ReadableResource source;
	private ZipEntry entry;
	private ResourceContainer<?> parent;

	public ZIPItem(ResourceContainer<?> parent, ReadableResource source, ZipEntry entry) {
		this.parent = parent;
		this.source = source;
		this.entry = entry;
	}

	@Override
	public String getContentType() {
		return URLConnection.guessContentTypeFromName(getName());
	}

	@SuppressWarnings("resource")
	@Override
	public ReadableContainer<ByteBuffer> getReadable() throws IOException {
		ZipInputStream stream = new ZipInputStream(IOUtils.toInputStream(source.getReadable()));
		try {
			ZipEntry entry = null;
			while ((entry = stream.getNextEntry()) != null) {
				if (entry.getName().equals(this.entry.getName())) {
					return IOUtils.wrap(stream);
				}
			}
			// if nothing is found by now, it is not in there
			stream.close();
			throw new IOException("Could not find item " + this.entry.getName() + " in the zip archive");
		}
		// only close the stream in event of an error
		catch (IOException e) {
			stream.close();
			throw e;
		}
	}

	@Override
	public long getSize() {
		return entry.getSize();
	}

	@Override
	public Date getLastModified() {
		return new Date(entry.getTime());
	}

	@Override
	public String getName() {
		return entry.getName().replaceAll("^.*?([^/]+)$", "$1");
	}

	@Override
	public ResourceContainer<?> getParent() {
		return parent;
	}

	@Override
	public void close() throws IOException {
		source.close();
	}
}
