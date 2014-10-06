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
