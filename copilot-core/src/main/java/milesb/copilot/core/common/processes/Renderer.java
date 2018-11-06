package milesb.copilot.core.common.processes;

import org.opencv.core.Mat;

import milesb.copilot.core.domain.TrackedObject;

public interface Renderer {
	void render(Mat image, TrackedObject imageObject);
	void init();
}
