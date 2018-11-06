package milesb.copilot.core.sign_alerter.identifier.data;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import milesb.copilot.core.sign_alerter.identifier.Descriptor;
import milesb.copilot.core.sign_alerter.identifier.Label;
import milesb.copilot.opencv.OpenCVLibrary;
import milesb.copilot.test.utils.TestFileResources;

public class TestFileSignDatabase {

	private FileSignDatabase signDatabase;

	@Before
	public void setup() throws IOException {
		OpenCVLibrary.loadLibrary();

		TestFileResources fileResources = new TestFileResources();
		signDatabase = new FileSignDatabase(fileResources);

	}

	@Test
	public void testDescriptorsLoaded() throws IOException {

		List<Descriptor> descriptors = signDatabase.getDescriptors();

		assertEquals(34, descriptors.size());
	}

	@Test
	public void testLoadLabels() throws IOException {
		List<Label> labels = signDatabase.getLabels();

		assertEquals(34, labels.size());
	}


}
