package be.nabu.libs.resources.zip;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import be.nabu.libs.resources.ResourceUtils;
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
	public ZIPItem getChild(String name) {
		try {
			return (ZIPItem) ResourceUtils.resolve(this, name);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
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
}