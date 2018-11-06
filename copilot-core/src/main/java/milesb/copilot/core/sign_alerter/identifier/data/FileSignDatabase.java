package milesb.copilot.core.sign_alerter.identifier.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milesb.copilot.core.common.FileResources;
import milesb.copilot.core.sign_alerter.identifier.Descriptor;
import milesb.copilot.core.sign_alerter.identifier.DescriptorGenerator;
import milesb.copilot.core.sign_alerter.identifier.DescriptorGeneratorFactoryImpl;
import milesb.copilot.core.sign_alerter.identifier.Label;
import milesb.copilot.core.sign_alerter.identifier.SignDatabase;
import milesb.copilot.core.sign_alerter.identifier.SignType;

public class FileSignDatabase implements SignDatabase {


	private static final String DEFAULT_LABELS_FILENAME = "labels.txt";
	private static final String DEFAULT_DESCRIPTORS_FILENAME = "descriptors.dat";

	private List<Descriptor> descriptors;
	private List<Label> labels;
	private Map<Integer, SignType> signTypes;

	private DescriptorGenerator descriptorGenerator;
	private int featureDetectorId;
	private int descriptorExtractorId;

	private String labelsFilename;
	private String descriptorsFilename;

	private FileResources fileResources;

	public FileSignDatabase(FileResources fileResources) {
		this.fileResources = fileResources;

		labelsFilename = DEFAULT_LABELS_FILENAME;
		descriptorsFilename = DEFAULT_DESCRIPTORS_FILENAME;

		load();

	}

	private void load() {
		loadDescriptors();
		loadLabels();
		loadIndexes();
	}

	private void loadIndexes() {

		signTypes = new HashMap<>();
		for (Label label : labels) {
			int signTypeId = label.getSignTypeId();
			SignType signType = new SignType(signTypeId, label.getText());
			signTypes.put(signTypeId, signType);

		}
	}

	public SignType getSignType(int signTypeId) {
		return signTypes.get(signTypeId);
	}

	private void loadDescriptors() {

		try {
			InputStream inputStream = fileResources.getInputStream(descriptorsFilename);

			DescriptorFile descriptorFile = new DescriptorFile();
			descriptorFile.load(inputStream);

			descriptors = descriptorFile.getDescriptors();

			featureDetectorId = descriptorFile.getFeatureDetectorId();
			descriptorExtractorId = descriptorFile.getDescriptorExtractorId();

			descriptorGenerator = DescriptorGeneratorFactoryImpl.getInstance()
					.createDescriptorGenerator(featureDetectorId, descriptorExtractorId);
		} catch (IOException e) {
			throw new SignDataException("Failed to load descriptors.", e);
		}

	}

	private void loadLabels() {

		try {
			InputStream inputStream = fileResources.getInputStream(labelsFilename);

			LabelFile labelFile = new LabelFile();
			labelFile.load(inputStream);

			labels = labelFile.getLabels();
		} catch (IOException e) {
			throw new SignDataException("Failed to load labels.", e);
		}
	}

	public void save() {

		writeDescriptors();
		writeLabels();

	}

	private void writeDescriptors() {
		DescriptorFile descriptorFile = new DescriptorFile();

		try {
			OutputStream outputStream = fileResources.getOutputStream(descriptorsFilename);

			descriptorFile.setFeatureDetectorId(this.descriptorExtractorId);
			descriptorFile.setDescriptorExtractorId(descriptorExtractorId);
			descriptorFile.setDescriptors(descriptors);

			descriptorFile.save(outputStream);
		} catch (IOException e) {
			throw new SignDataException("Failed to write descriptors", e);
		}

	}

	private void writeLabels() {
		LabelFile labelFile = new LabelFile();
		try {
			OutputStream outputStream = fileResources.getOutputStream(labelsFilename);
			labelFile.setLabels(labels);
			labelFile.save(outputStream);
		} catch (IOException e) {
			throw new SignDataException("Unable to write labels", e);
		}
	}

	public DescriptorGenerator getDescriptorGenerator() {
		return descriptorGenerator;
	}

	public List<Descriptor> getDescriptors() {
		return descriptors;
	}

	public List<Label> getLabels() {
		return labels;
	}

}
