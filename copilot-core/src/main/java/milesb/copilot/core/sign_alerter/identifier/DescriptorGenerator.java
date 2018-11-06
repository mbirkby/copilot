package milesb.copilot.core.sign_alerter.identifier;

import org.opencv.core.Mat;

public interface DescriptorGenerator {
	Mat calcDescriptors(Mat image);
}
