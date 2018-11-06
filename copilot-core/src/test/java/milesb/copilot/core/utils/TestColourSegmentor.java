package milesb.copilot.core.utils;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import milesb.copilot.opencv.OpenCVLibrary;

public class TestColourSegmentor {

	private ColourSegmentor segmentor;

	@BeforeClass
	public static void initLibrary() {
		System.out.println(System.getProperty("java.library.path"));
		OpenCVLibrary.loadLibrary();
	}

	@Before
	public void setup() {

		segmentor = new ColourSegmentor();

	}

	@Test
	public void testInRangeForPositiveBytes() {
		byte[] input = { 2, 3, 4, 99, 100, 101 };

		assertFalse("Test 1 - false if 1 less than equal to low", segmentor.inRange(input[0], 3, 100));
		assertTrue("Test 2 - true if equal to low", segmentor.inRange(input[1], 3, 100));
		assertTrue("Test 3 - false if 1 greater than low", segmentor.inRange(input[2], 3, 100));

		assertTrue("Test 4 - true if one less than high", segmentor.inRange(input[3], 3, 100));
		assertFalse("Test 5 -false if equal to high", segmentor.inRange(input[4], 3, 100));
		assertFalse("Test 6 - false if one more than high", segmentor.inRange(input[5], 3, 100));

	}

	@Test
	public void testInRangeForNegativeBytes() {
		byte[] input = { -127, -126, -125, -117, -116, -115 };// 129,130,131,139,140,141

		assertFalse("Test 1 - false if 1 less than low", segmentor.inRange(input[0], 130, 140));
		assertTrue("Test 2 - true if 1 equal to low", segmentor.inRange(input[1], 130, 140));
		assertTrue("Test 3 - true if one greater than low", segmentor.inRange(input[2], 130, 140));

		assertTrue("Test 4 - true if one less than high", segmentor.inRange(input[3], 130, 140));
		assertFalse("Test 5 - false if equal to high", segmentor.inRange(input[4], 130, 140));
		assertFalse("Test 6 - false if one more than high", segmentor.inRange(input[5], 130, 140));

	}

	@Test
	public void testInRangeSpanning128() {
		byte[] input = { 126, 127, -128, -127, -126, -125 };// 126,127,128,129,130

		assertFalse("Test 1 - true if 1 less than low", segmentor.inRange(input[0], 127, 129));
		assertTrue("Test 2 - true if equal to low", segmentor.inRange(input[1], 127, 129));
		assertTrue("Test 3 - fal if one greater than low", segmentor.inRange(input[2], 127, 129));

		assertTrue("Test 4 - true if one less than  high", segmentor.inRange(input[2], 127, 129));
		assertFalse("Test 5 - false if equal to high", segmentor.inRange(input[3], 127, 129));
		assertFalse("Test 6 - false if one more than high", segmentor.inRange(input[4], 127, 129));

	}

	@Test
	public void testInputByte() {
		segmentor.setChannelRange(0, 10, 181);
		segmentor.setChannelRange(1, 100, 256);
		segmentor.setChannelRange(2, 60, 256);

		assertTrue("All bytes should be statisfied", segmentor.inRange(new byte[] { 20, -1, -1 }));
		assertFalse("First byte not satisfied", segmentor.inRange(new byte[] { 9, -1, -1 }));
		assertFalse("Second byte not satisfied", segmentor.inRange(new byte[] { 20, 99, -1 }));
		assertFalse("Third byte not satisfied", segmentor.inRange(new byte[] { 20, -1, 59 }));
		assertFalse("First and Second bytes not satisfied", segmentor.inRange(new byte[] { 9, 99, -1 }));
		assertFalse("First and Third bytes not satisfied", segmentor.inRange(new byte[] { 9, -1, 59 }));
		assertFalse("Second and Third bytes not satisfied", segmentor.inRange(new byte[] { 20, 99, 59 }));
		assertFalse("No bytes satisfied", segmentor.inRange(new byte[] { 9, 99, 59 }));

	}

	@Test
	public void testSegment() {

		// setup segmentor
		segmentor.setChannelRange(0, 10, 170);// channel 0, low value, high value
		segmentor.setChannelRange(1, 100, 256);
		segmentor.setChannelRange(2, 60, 256);

		segmentor.setSize(new Size(3, 3)); // expects 3 x 3 image
		byte[] insideRangeColour = segmentor.getInsideRangeColour();
		byte[] outsideRangeColour = segmentor.getOutsideRangeColour();

		// Create a basic 3 x 3 image to test
		Mat image = new Mat(3, 3, CvType.CV_8UC3);

		image.put(0, 0, new byte[] { 20, 110, 70 });
		image.put(0, 1, new byte[] { 30, 110, 70 });
		image.put(0, 2, new byte[] { 40, 110, 70 });

		for (int row = 1; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				image.put(row, col, new byte[] { 0, 110, 70 });
			}
		}

		// run code
		Mat segmented = segmentor.segment(image);

		// test results
		byte[] segmentedPixel = new byte[1];
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				segmented.get(row, col, segmentedPixel);

				if (row == 0) {
					Assert.assertArrayEquals(insideRangeColour, segmentedPixel);
				} else {
					Assert.assertArrayEquals(outsideRangeColour, segmentedPixel);
				}
			}
		}

	}

	@Test
	public void testInvert() {
		// set the range of hue >=10 AND hue < 170
		segmentor.setChannelRange(0, 10, 170);
		segmentor.setChannelRange(1, 100, 256);
		segmentor.setChannelRange(2, 60, 256);

		// logic should set true for hue channel if ((hue >=0) AND (hue < 10)) OR ((hue
		// >= 170) AND (hue < 180))
		// we assume hue is always >=0 and < 180 if it wasn't it would always be outside
		// range
		segmentor.setInvertChannel(0, true);

		assertTrue("Just inside lower range", segmentor.inRange(new byte[] { 9, 110, 70 }));
		assertFalse("Just outside lower range", segmentor.inRange(new byte[] { 10, 110, 70 }));

		assertFalse("Just outside upper range", segmentor.inRange(new byte[] { (byte) 169, 110, 70 }));
		assertTrue("Just inside upper range", segmentor.inRange(new byte[] { (byte) 170, 110, 70 }));

	}

}
