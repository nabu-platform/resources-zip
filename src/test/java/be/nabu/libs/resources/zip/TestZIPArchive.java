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
