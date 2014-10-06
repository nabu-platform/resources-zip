package be.nabu.libs.resources.zip;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import be.nabu.libs.resources.ResourceFactory;
import be.nabu.libs.resources.api.ResourceResolver;
import be.nabu.libs.resources.api.ResourceRoot;

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
	public ResourceRoot getResource(URI uri, Principal principal) throws IOException {
		try {
			ZIPArchive zipArchive = new ZIPArchive();
			ResourceRoot resource = ResourceFactory.getInstance().resolve(new URI(uri.getSchemeSpecificPart()), principal);
			zipArchive.setSource(resource);
			return zipArchive;
		}
		catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}
}
