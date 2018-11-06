package milesb.copilot.core.utils;

import org.opencv.core.Mat;
import org.opencv.core.Size;

public interface Segmentor {
	Mat segment(Mat image);// returns a segmented image
	void init();

}
