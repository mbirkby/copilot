package milesb.copilot.core.domain;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

public class Contours {
	private List<MatOfPoint> contours;
	private Mat hierarchy;

	public Contours() {
		contours = new ArrayList<>();
		hierarchy = new Mat();
	}
	
	public List<MatOfPoint> getContours() {
		return contours;
	}

	public void setContours(List<MatOfPoint> contours) {
		this.contours = contours;
	}

	public Mat getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Mat hierarchy) {
		this.hierarchy = hierarchy;
	}

}
