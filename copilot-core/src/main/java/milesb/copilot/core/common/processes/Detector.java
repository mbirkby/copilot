package milesb.copilot.core.common.processes;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Size;

import milesb.copilot.core.domain.AreaOfInterest;

public interface Detector {
	List<AreaOfInterest> detect(Mat colourImage);
	void init();
}
