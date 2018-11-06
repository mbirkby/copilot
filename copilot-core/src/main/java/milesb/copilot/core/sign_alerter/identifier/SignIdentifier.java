package milesb.copilot.core.sign_alerter.identifier;


import milesb.copilot.core.common.processes.Identifier;
import milesb.copilot.core.domain.Sign;
import milesb.copilot.core.domain.TrackedObject;

import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class SignIdentifier implements Identifier {

	private SignDatabase signDatabase;
	private SignIdentifierMatcher signMatcher;
	private DescriptorGenerator descriptorGenerator;

	public SignIdentifier(SignDatabase signDatabase) {

		this.signDatabase = signDatabase;
	}

	public SignIdentifierMatcher getSignMatcher() {
		return signMatcher;
	}

	public void setSignMatcher(SignIdentifierMatcher signMatcher) {
		this.signMatcher = signMatcher;
	}

	public DescriptorGenerator getDescriptorGenerator() {
		return descriptorGenerator;
	}

	public void setDescriptorGenerator(DescriptorGenerator descriptorGenerator) {
		this.descriptorGenerator = descriptorGenerator;
	}

	public void identify(List<TrackedObject> trackedObjects) {

		for (TrackedObject trackedObject : trackedObjects) {
			Sign sign = (Sign) trackedObject;
			if (!sign.isIdentified()) {
				identify(sign);
			}
		}

	}

	@Override
	public void init() {

	}

	void identify(Sign sign) {


		Mat greyScale = getGreyscaleImage(sign);


		Mat descriptors = calcDescriptors(greyScale);


		Descriptor descriptor = findMatchingDescriptor(descriptors);


		SignType signType = getSignTypeForDescriptor(descriptor);

		assignSignTypeToSign(signType, sign);

	}

	private SignType getSignTypeForDescriptor(Descriptor descriptor) {
		SignType signType = null;
		if (descriptor != null) {
			signType = signDatabase.getSignType(descriptor.getSignTypeId());
		}

		return signType;
	}

	private void assignSignTypeToSign(SignType signType, Sign sign) {
		sign.setSignType(signType);
	}

	private Mat getGreyscaleImage(Sign sign) {

		Mat bgrImage = sign.getImage();

		Mat greyImage = new Mat(bgrImage.size(), CvType.CV_8UC1);

		Imgproc.cvtColor(bgrImage, greyImage, Imgproc.COLOR_BGR2GRAY);

		return greyImage;
	}

	private Mat calcDescriptors(Mat grey) {

		return descriptorGenerator.calcDescriptors(grey);
	}

	private Descriptor findMatchingDescriptor(Mat imageDescriptors) {
		return signMatcher.match(imageDescriptors);

	}

}
