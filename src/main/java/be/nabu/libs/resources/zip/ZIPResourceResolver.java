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
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import be.nabu.libs.resources.ResourceFactory;
import be.nabu.libs.resources.api.Resource;
import be.nabu.libs.resources.api.ResourceResolver;

/**
 * Use zip:file:/path/to/file.zip
 */
public class ZIPResourceResolver implements ResourceResolver {

	private static List<String> defaultSchemes = Arrays.asList(new String [] { "zip" });
	
	@Override
	public List<String> getDefaultSchemes() {
		return defaultSchemes;
	}

	@Override
	public Resource getResource(URI uri, Principal principal) throws IOException {
		try {
			ZIPArchive zipArchive = new ZIPArchive();
			Resource resource = ResourceFactory.getInstance().resolve(new URI(uri.getSchemeSpecificPart()), principal);
			zipArchive.setSource(resource);
			return zipArchive;
		}
		catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}
}
