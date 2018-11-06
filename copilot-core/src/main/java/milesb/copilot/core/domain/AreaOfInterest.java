package milesb.copilot.core.domain;

import org.opencv.core.Mat;

import org.opencv.core.Point;
import org.opencv.core.Rect;

public class AreaOfInterest {

	private int id;
	private Mat image;
	private Rect boundingRect;
	private Mat descriptors;
	private Point centreLocation;

	public AreaOfInterest(int id, Mat image, Point centreLocation) {
		if (centreLocation == null) {
			throw new InvalidAreaOfInterestException("Null centreLocation passed into constructor");
		}

		if (image == null) {
			throw new InvalidAreaOfInterestException("Null image passeed in to constructor");
		}

		this.id = id;

		this.image = new Mat(image.size(), image.type());
		image.copyTo(this.image);

		this.centreLocation = new Point(centreLocation.x, centreLocation.y);

		updateBoundingRect();
	}

	public AreaOfInterest(int id, Mat image, Rect boundingRect) {
		if (boundingRect == null) {
			throw new InvalidAreaOfInterestException("Null boundingREct passed into constructor");
		}

		if (image == null) {
			throw new InvalidAreaOfInterestException("Null image passeed in to constructor");
		}

		if (boundingRect.width != image.cols()) {
			throw new InvalidAreaOfInterestException(
					"BoundingRect width " + boundingRect.width + " not compatible with image width of " + image.cols());
		}

		if (boundingRect.height != image.rows()) {
			throw new InvalidAreaOfInterestException("BoundingRect height " + boundingRect.height
					+ " not compatible with image height of " + image.rows());
		}

		this.id = id;
		this.image = new Mat(image.size(), image.type());
		image.copyTo(this.image);

		this.boundingRect = new Rect(boundingRect.tl(), boundingRect.br());

		updateCentreLocation();
	}

	private void updateBoundingRect() {

		if (image != null) {
			if (centreLocation == null) {
				centreLocation = new Point(image.width() / 2, image.height() / 2);
			}

			boundingRect = ObjectUtils.centrePointToRect(centreLocation, image.size());

		} else {
			throw new InvalidAreaOfInterestException("No image set: cannot update BoundingRect");
		}

	}

	private void updateCentreLocation() {
		if (image != null) {
			if (boundingRect != null) {
				centreLocation = ObjectUtils.rectToCentrePoint(boundingRect);
			} else {
				throw new InvalidAreaOfInterestException("No boundingRect set : cannot update centreLocation");
			}
		} else {
			throw new InvalidAreaOfInterestException("No image set: cannot update centreLocation");
		}
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
		AreaOfInterest other = (AreaOfInterest) obj;

		return id == other.id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Point getCentreLocation() {
		return centreLocation;
	}

	public void setDescriptors(Mat desc) {
		descriptors = desc;
	}

	

	public Mat getDescriptors() {
		return descriptors;
	}

	

	public Rect getBoundingRect() {
		return boundingRect;
	}

	

	public Mat getImage() {
		return image;
	}

}
