package milesb.copilot.core.sign_alerter.identifier;

import org.opencv.core.Mat;

public interface SignIdentifierMatcher {
	Descriptor match(Mat imageDescriptors);
}
