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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import junit.framework.TestCase;
import be.nabu.libs.resources.ResourceFactory;
import be.nabu.libs.resources.ResourceUtils;
import be.nabu.libs.resources.api.ReadableResource;
import be.nabu.libs.resources.api.Resource;
import be.nabu.utils.io.IOUtils;

public class TestZIPArchive extends TestCase {
	
	public void testArchive() throws IOException, URISyntaxException {
		Resource zip = ResourceFactory.getInstance().resolve(new URI("zip:classpath:/test.zip"), null); 
		ReadableResource resource = (ReadableResource) ResourceUtils.resolve(zip, "test1.txt");
		assertNotNull(resource);
		assertEquals("wtf", IOUtils.toString(IOUtils.wrapReadable(resource.getReadable(), Charset.forName("UTF-8"))));
		
		assertNull(ResourceUtils.resolve(zip, "test2.txt"));
		
		resource = (ReadableResource) ResourceUtils.resolve(zip, "dir1/dir2/file1.txt");
		assertNotNull(resource);
		assertEquals("test", IOUtils.toString(IOUtils.wrapReadable(resource.getReadable(), Charset.forName("UTF-8"))));
	}
}
