package milesb.copilot.core.sign_alerter.detector;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import milesb.copilot.core.domain.AreaOfInterest;
import milesb.copilot.core.domain.Contours;
import milesb.copilot.opencv.OpenCVLibrary;

public class TestSignDetector {
	private final double DELTA = 0.05;
	private SignDetector signDetector;

	public static boolean loadedLibrary = false;

	@BeforeClass
	public static void init() {
		OpenCVLibrary.loadLibrary();
	}

	@Before
	public void setup() {
		signDetector = new SignDetector();

	}

	@Test
	public void testDefaultProperties() {
		assertEquals("Min area", 1000, signDetector.getMinAreaInPixels());
		assertEquals("Max area", 15000, signDetector.getMaxAreaInPixels());
		assertEquals("Min aspect ratio", 0.6, signDetector.getMinAspectRatio(), DELTA);
		assertEquals("Max aspect ratio", 1.3, signDetector.getMaxAspectRatio(), DELTA);

	}

	@Test
	public void testTooBig() {
		assertFalse(signDetector.isTooBig(14999));
		assertFalse(signDetector.isTooBig(15000));
		assertTrue(signDetector.isTooBig(15001));
	}

	@Test
	public void testTooSmall() {
		assertTrue(signDetector.isTooSmall(999));
		assertFalse(signDetector.isTooSmall(1000));
		assertFalse(signDetector.isTooSmall(1001));
	}

	@Test
	public void testTooTall() {
		// lets say width = 1
		assertFalse("Aspect ratio 1.2 < 1.3 so not too tall", signDetector.isTooTall(1.2)); // height = 1.2
		assertFalse("Aspect ratio 1.3 = 1.3  so not too tall", signDetector.isTooTall(1.3)); // height = 1.3
		assertTrue("Aspect ratio 1.4  not less tahn 1.3 so it is too tall", signDetector.isTooTall(1.4)); // height =
																											// 1.4
	}

	@Test
	public void testTooWide() {
		// lets say height = 1,
		assertTrue("Aspect Ratio 0.5 < 0.6 so it is too wide", signDetector.isTooWide(0.5));// width = 2.0
		assertFalse("Aspect Ratio 0.6 = 0.6 so not too wide", signDetector.isTooWide(0.6));// width = 1.67
		assertFalse("Aspect Ratio 0.7  > 0.6 so should return false", signDetector.isTooWide(0.7));// width = 1.43
	}

	@Test
	public void testIsAreaOK() {
		assertFalse("Area slightly smaller tha minimum", signDetector.isAreaOK(999));
		assertTrue("Area equals minimum", signDetector.isAreaOK(1000));
		assertTrue("Area slightly higher than minimum", signDetector.isAreaOK(1001));

		assertTrue("Area slightly smaller tha maximum", signDetector.isAreaOK(14999));
		assertTrue("Area equals maximum", signDetector.isAreaOK(15000));
		assertFalse("Area slightly higher than maximum", signDetector.isAreaOK(15001));
	}

	@Test
	public void testIsAspectRatioOK() {
		assertFalse("Aspect ratio slightly smaller tha minimum", signDetector.isAspectRatioOK(0.5));
		assertTrue("Aspect ratio equals minimum", signDetector.isAspectRatioOK(0.6));
		assertTrue("Aspect ratio slightly higher than minimum", signDetector.isAspectRatioOK(0.7));

		assertTrue("Aspect ratio slightly smaller tha maximum", signDetector.isAspectRatioOK(1.2));
		assertTrue("Aspect ratio equals maximum", signDetector.isAspectRatioOK(1.3));
		assertFalse("Aspect ratio slightly higher than maximum", signDetector.isAspectRatioOK(1.4));
	}

	@Test
	public void testFindAreasOfInterest() {
		Mat image = new Mat(1000, 800, CvType.CV_8UC3);
		List<MatOfPoint> listContours = new ArrayList<>();

		listContours.add(createMatOfPointsForRectangle(0, 0, 32, 32));
		listContours.add(createMatOfPointsForRectangle(32, 0, 30, 32));// too small
		listContours.add(createMatOfPointsForRectangle(62, 100, 130, 130));// too large
		listContours.add(createMatOfPointsForRectangle(192, 100, 32, 42));// too tall
		listContours.add(createMatOfPointsForRectangle(224, 100, 60, 32));// too wide

		Contours contours = new Contours();
		contours.setContours(listContours);

		List<AreaOfInterest> areasOfInterest = signDetector.getAreasOfInterest(image, contours);

		assertEquals("Should return 1", 1, areasOfInterest.size());
		assertEquals("Incorrect width", 32, areasOfInterest.get(0).getBoundingRect().width);
		assertEquals("Incorrect height", 32, areasOfInterest.get(0).getBoundingRect().height);

	}

	private MatOfPoint createMatOfPointsForRectangle(int x, int y, int width, int height) {
		List<Point> points = new ArrayList<>();
		points.add(new Point(x, y));
		points.add(new Point(x + width - 1, y));
		points.add(new Point(x + width - 1, y + height - 1));
		points.add(new Point(x, y + height - 1));
		MatOfPoint mop = new MatOfPoint();
		mop.fromList(points);

		return mop;

	}

}
