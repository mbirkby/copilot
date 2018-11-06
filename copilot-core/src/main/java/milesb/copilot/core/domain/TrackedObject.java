package milesb.copilot.core.domain;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

public class TrackedObject {

	private int id;
	private String label;
	private int numFramesUnmatched;

	private Rect boundingRect;
	private Size size;
	private Point centreLocation;
	private Mat image;

	private boolean newTrackedObject;
	private boolean matchedInCurrentFrame;

	public TrackedObject() {
		newTrackedObject = true;
		matchedInCurrentFrame = true;
	}

	public boolean isNewTrackedObject() {
		return newTrackedObject;
	}

	public void setNewTrackedObject(boolean newTrackedObject) {
		this.newTrackedObject = newTrackedObject;
	}

	public boolean isMatchedInCurrentFrame() {
		return matchedInCurrentFrame;
	}

	public void setMatchedInCurrentFrame(boolean matchedInCurrentFrame) {
		this.matchedInCurrentFrame = matchedInCurrentFrame;

	}

	public void setAreaOfInterest(AreaOfInterest areaOfInterest) {

		setImage(areaOfInterest.getImage());
		setBoundingRect(areaOfInterest.getBoundingRect());

	}

	public void setImage(Mat image) {
		this.image = new Mat(image.size(), image.type());
		image.copyTo(this.image);
	}

	public Mat getImage() {
		return image;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrackedObject other = (TrackedObject) obj;
		return id == other.id;
	}

	public void setBoundingRect(Rect boundingRect) {
		this.boundingRect = new Rect(boundingRect.tl(), boundingRect.br());
		centreLocation = ObjectUtils.rectToCentrePoint(boundingRect);
		size = new Size(boundingRect.width, boundingRect.height);
	}

	public Point getCentreLocation() {
		return centreLocation;
	}

	public Rect getBoundingRect() {
		return boundingRect;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void incrementNumFramesUnmatched() {
		numFramesUnmatched++;
	}

	public void resetNumFramesUnmatched() {
		numFramesUnmatched = 0;
	}

	public int getNumFramesUnmatched() {
		return numFramesUnmatched;
	}

	public String getLabel() {
		if (label == null) {
			return "[" + id + "]";
		} else {
			return label;
		}
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean hasAlert() {
		return isNewTrackedObject();
	}

	public Size getSize() {
		return size;
	}

}
