package milesb.copilot.core.utils;


import org.opencv.core.CvType;
import org.opencv.core.Mat;

import org.opencv.core.Size;

import milesb.copilot.core.CopilotConfig;

/*
 * Creates a binary image of just 2 colours (usually black and white) from a 3 channel (colour image). It is used 
 * to find regions of a particular colour.  The client application sets the low and high limits of each channel 
 * (and optionally, the invertChannel value) and when run it tests each channel against the the set limits.  
 * If the test on all the channels returns true then the output pixel is set to insideRangeColour, otherwise it is
 * set to outsideRangeColour.
 * 
 * It is possible to invert the result of the test for a given channel by setting the invertChannel for that channel to true
 * In this case the test returns true if it outside the colour range for that channel and false otherwise.  
 * 
 * An example of using  the invert feature is when segmenting in Hue, Saturation, Value (HSV) colour spaces to find
 * areas of a particular colour where the Hue  in the range 0-179 on a colour wheel(in opencv at least).
 * Red values have hues 0-10 and 170-179 (in opencv at least) so if looking for reds its easier
 * to say the pixel is red if its hue value is NOT in the range 11-169.  This can be achieved by setting the
 * invertChannel[0] to true.
 * 

 */
public class ColourSegmentor implements Segmentor {
	public static final int AUTO_THRESHOLD = -1;
	public static final String PROP_THRESHOLD = "threshold";
	private static final byte[] WHITE = { (byte) 255 };
	private static final byte[] BLACK = { (byte) 0 };

	private int[] lowArray;
	private int[] highArray;

	private Mat segmented;

	private byte[] insideRangeColour;
	private byte[] outsideRangeColour;

	private byte[] pixelValue;
	private int imageWidth;
	private int imageHeight;

	private boolean[] invertChannel; 

	public ColourSegmentor() {
		lowArray = new int[3];
		highArray = new int[3];
		invertChannel = new boolean[3];
		pixelValue = new byte[3];

		setDefaultColours();

	}

	
	/*INIT*/
	public void init() {
		Size size = CopilotConfig.getInstance().getFrameSize();
		setSize(size);		
	}
	
	public void setSize(Size size) {

		segmented = new Mat(size, CvType.CV_8UC1);
		imageWidth = (int)size.width;
		imageHeight = (int)size.height;
	}
	
	
	/* SEGMENT IMAGE */
	
	@Override
	public Mat segment(Mat image) {

		applyThresholds(image, segmented);

		return segmented;

	}
	
	private void applyThresholds(Mat inputImage, Mat outputImage) {


		for (int col = 0; col < imageWidth; col++) {
			for (int row = 0; row < imageHeight; row++) {
				inputImage.get(row, col, pixelValue);
				if (inRange(pixelValue)) {
					outputImage.put(row, col, insideRangeColour);
				} else {
					outputImage.put(row, col, outsideRangeColour);
				}
			}
		}
	}
	
	
	
	/*TESTING RANGE*/
	
	boolean inRange(byte[] input) {
		boolean inRange = true;
		int channel = 0;
		while (channel < input.length && inRange) {
			inRange = inRange(input[channel], lowArray[channel], highArray[channel]);
			if (invertChannel[channel]) {
				inRange = !inRange;
			}
			channel++;
		}

		return inRange;
	}
	
	boolean inRange(byte input, int low, int high) {
		//int inputInt = Byte.toUnsignedInt(input);
		int inputInt = input & 0xFF;

		return (inputInt >= low && inputInt < high);
	}

	
	/*GETTERS AND SETTERS*/
	
	public void setInvertChannel(int channel, boolean invert) {
		invertChannel[channel] = invert;
	}

	public int getChannelLow(int channel) {
		return lowArray[channel];
	}

	public int getChannelHigh(int channel) {
		return highArray[channel];
	}

	public void invertColours() {
		insideRangeColour = BLACK;
		outsideRangeColour = WHITE;
	}

	private void setDefaultColours() {
		insideRangeColour = WHITE;
		outsideRangeColour = BLACK;
	}

	byte[] getInsideRangeColour() {
		return insideRangeColour;
	}

	byte[] getOutsideRangeColour() {
		return outsideRangeColour;
	}

	public void setThresholds(int[] low, int[] high) {
		for (int i = 0; i < 3; i++) {
			lowArray[i] = low[i];
			highArray[i] = high[i];
		}

	}

	public void setChannelRange(int channel, int low, int high) {

		lowArray[channel] = low;
		highArray[channel] = high;
	}

	

	

	

	

}
