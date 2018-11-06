package milesb.copilot.core.sign_alerter.identifier;

import org.opencv.core.Mat;

public class Descriptor {
	private int signTypeId;
	private Mat descriptor;

	public Descriptor() {
		this.signTypeId = -1;

		this.descriptor = null;
	}

	public Descriptor(int signTypeId, Mat descriptor) {
		this.signTypeId = signTypeId;
		this.descriptor = descriptor;
	}

	public int getSignTypeId() {
		return signTypeId;
	}

	public Mat getDescriptor() {
		return descriptor;
	}

	public void setSignTypeId(int signTypeId) {
		this.signTypeId = signTypeId;
	}

	public void setDescriptor(Mat descriptor) {
		this.descriptor = descriptor;
	}

}
