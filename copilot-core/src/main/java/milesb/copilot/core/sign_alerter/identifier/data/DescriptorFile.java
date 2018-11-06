package milesb.copilot.core.sign_alerter.identifier.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;

import milesb.copilot.core.sign_alerter.identifier.Descriptor;

public class DescriptorFile {

	private MatIO matIO;
	private int featureDetectorId;
	private int descriptorExtractorId;
	private int numRecords;
	private List<Descriptor> descriptors;

	DescriptorFile() {
		matIO = new MatIO();
	}

	void load(InputStream inputStream) throws IOException {
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		try {

			loadHeader(dataInputStream);
			loadDescriptors(dataInputStream);

		} finally {
			dataInputStream.close();
		}

	}

	private void loadHeader(DataInputStream dis) throws IOException {
		numRecords = dis.readInt();
		featureDetectorId = dis.readInt();
		descriptorExtractorId = dis.readInt();
	}

	private void loadDescriptors(DataInputStream dis) throws IOException {
		descriptors = new ArrayList<>();

		for (int i = 0; i < numRecords; i++) {
			int id = dis.readInt();
			Mat imageDescriptors = matIO.readMat(dis);

			Descriptor descriptor = new Descriptor(id, imageDescriptors);

			descriptors.add(descriptor);
		}

	}

	void save(OutputStream outputStream) throws IOException {

		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		try {
			writeHeader(dataOutputStream);
			writeDescriptors(dataOutputStream);

		} finally {
			dataOutputStream.close();
		}

	}

	private void writeHeader(DataOutputStream dos) throws IOException {
		dos.writeInt(getNumRecords());
		dos.writeInt(getFeatureDetectorId());
		dos.writeInt(getDescriptorExtractorId());
	}

	private void writeDescriptors(DataOutputStream dos) throws IOException {

		for (Descriptor descriptor : descriptors) {
			dos.writeInt(descriptor.getSignTypeId());
			matIO.writeMat(descriptor.getDescriptor(), dos);
		}

	}

	public List<Descriptor> getDescriptors() {
		return descriptors;
	}

	public void setDescriptors(List<Descriptor> descriptors) {
		this.descriptors = descriptors;
	}

	int getFeatureDetectorId() {
		return featureDetectorId;
	}

	void setFeatureDetectorId(int featureDetectorId) {
		this.featureDetectorId = featureDetectorId;
	}

	int getDescriptorExtractorId() {
		return descriptorExtractorId;
	}

	void setDescriptorExtractorId(int descriptorExtractorId) {
		this.descriptorExtractorId = descriptorExtractorId;
	}

	private int getNumRecords() {
		return descriptors.size();
	}

}
