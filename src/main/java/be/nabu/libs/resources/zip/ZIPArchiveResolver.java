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

import java.util.Arrays;
import java.util.List;

import be.nabu.libs.resources.api.Archive;
import be.nabu.libs.resources.api.ArchiveResolver;
import be.nabu.libs.resources.api.Resource;

public class ZIPArchiveResolver implements ArchiveResolver {

	private static List<String> supportedContentTypes = Arrays.asList(new String [] { 
		"application/zip", 
		"multipart/x-zip",
		"application/vnd.oasis.opendocument.text"
	});
	
	@Override
	public Archive<Resource> newInstance() {
		return new ZIPArchive();
	}

	@Override
	public List<String> getSupportedContentTypes() {
		return supportedContentTypes;
	}
}
