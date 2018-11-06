package milesb.copilot.test.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import milesb.copilot.core.common.FileResources;

public class TestFileResources implements FileResources {

	private static final String DEFAULT_RESOURCE_FOLDER = "src/test/resources/database";

	private File resourceFolder;

	public TestFileResources() {
		resourceFolder = new File(DEFAULT_RESOURCE_FOLDER);
	}

	public File getResourceFolder() {
		return resourceFolder;
	}

	public void setResourceFolder(File resourceFolder) {
		this.resourceFolder = resourceFolder;
	}

	@Override
	public InputStream getInputStream(String name) throws IOException {
		InputStream inputStream;
		File file = getFile(name);
		if (file != null) {
			inputStream = new FileInputStream(file);
		} else {
			throw new IOException("No input stream associated with name " + name);
		}

		return inputStream;
	}

	private File getFile(String name) {
		File[] files = resourceFolder.listFiles();
		File foundFile = null;

		for (File file : files) {
			if (file.isFile() && file.getName().toLowerCase().startsWith(name)) {
				foundFile = file;
				break;
			}
		}

		return foundFile;
	}

	@Override
	public OutputStream getOutputStream(String name) throws IOException {
		OutputStream outputStream;
		File file = getFile(name);
		if (file == null) {
			file = new File(resourceFolder, name);
		}

		outputStream = new FileOutputStream(file, false);

		return outputStream;
	}

}
