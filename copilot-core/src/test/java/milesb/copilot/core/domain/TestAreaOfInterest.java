package milesb.copilot.core.domain;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;


import milesb.copilot.opencv.OpenCVLibrary;

public class TestAreaOfInterest {

	@BeforeClass
	public static void init() {
		OpenCVLibrary.loadLibrary();
	}

	@Test
	public void testConstructorWithCentreLocation() {
		Mat image = new Mat(30, 20, CvType.CV_8UC3);
		Point centre = new Point(100, 150);

		AreaOfInterest areaOfInterest = new AreaOfInterest(0, image, centre);

		assertNotNull("Image is null", areaOfInterest.getImage());
		assertEquals("Image returned is different size from original", image.size(), areaOfInterest.getImage().size());
		assertEquals("Image returned is different type as original", image.type(), areaOfInterest.getImage().type());
		assertFalse("Image returned is different type as original", areaOfInterest.getImage() == image);

		Rect boundingRect = areaOfInterest.getBoundingRect();
		assertNotNull("boundingRect is null", boundingRect);
		assertEquals("boundingRect size different from image", image.size(), boundingRect.size());
		assertEquals("boundingRect x position is incorrect", 90, boundingRect.tl().x, 0.05);
		assertEquals("boundingRect y position is incorrect", 135, boundingRect.tl().y, 0.05);

	}

	@Test
	public void testConstructorWithRect() {
		Mat image = new Mat(30, 20, CvType.CV_8UC3);
		Rect rect = new Rect(90, 135, 20, 30);

		AreaOfInterest areaOfInterest = new AreaOfInterest(0, image, rect);

		assertNotNull("Image is null", areaOfInterest.getImage());
		assertEquals("Image returned is different size from original", image.size(), areaOfInterest.getImage().size());
		assertEquals("Image returned is different type as original", image.type(), areaOfInterest.getImage().type());
		assertFalse("Image returned is different object from original", areaOfInterest.getImage() == image);

		Point centre = areaOfInterest.getCentreLocation();
		assertNotNull("Cente point should not be null", centre);
		assertEquals("Centre x coordinate incorrect", 100, centre.x, 0.05);
		assertEquals("Centre y coordinate incorrect", 150, centre.y, 0.05);

	}

	@Test(expected = InvalidAreaOfInterestException.class)
	public void testInvalidRectReturnsInvalidAreaOfInterestException() {
		Mat image = new Mat(30, 20, CvType.CV_8UC3);// 30 rows by 20 columns so width is 20, height is 30
		Rect rect = new Rect(90, 135, 30, 20);// width is 30, height is 20 so doesn't match image

		new AreaOfInterest(0, image, rect);
	}

	@Test(expected = InvalidAreaOfInterestException.class)
	public void testNullImageReturnsInvalidAreaOfInterestException() {
		new AreaOfInterest(0, null, new Rect(90, 135, 30, 20));
	}

	@Test(expected = InvalidAreaOfInterestException.class)
	public void testNullCentreLocationReturnsInvalidAreaOfInterestException() {
		Mat image = new Mat(30, 20, CvType.CV_8UC3);
		Point centre = null;
		new AreaOfInterest(0, image, centre);
	}

	@Test(expected = InvalidAreaOfInterestException.class)
	public void testNullRectReturnsInvalidAreaOfInterestException() {
		Mat image = new Mat(30, 20, CvType.CV_8UC3);
		Rect rect = null;
		new AreaOfInterest(0, image, rect);
	}

}
