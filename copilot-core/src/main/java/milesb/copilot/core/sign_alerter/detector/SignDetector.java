package milesb.copilot.core.sign_alerter.detector;

import java.util.ArrayList;

import java.util.List;


import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import org.opencv.core.Rect;


import org.opencv.imgproc.Imgproc;



import milesb.copilot.core.CopilotConfig;

import milesb.copilot.core.utils.ColourSegmentor;
import milesb.copilot.core.common.processes.Detector;
import milesb.copilot.core.domain.AreaOfInterest;
import milesb.copilot.core.domain.Contours;

public class SignDetector implements Detector {

	private static final int HUE = 0;
	private static final int SAT = 1;
	private static final int VAL = 2;

	private static final int RANGE_LOW = 0;
	private static final int RANGE_HIGH = 1;

	private static final int[] DEFAULT_HUE_RANGE = { 10, 160 };
	private static final int[] DEFAULT_SAT_RANGE = { 100, 256 };
	private static final int[] DEFAULT_VAL_RANGE = { 60, 156 };

	private static boolean DEFAULT_HUE_INVERT = true;
	private static boolean DEFAULT_SAT_INVERT = false;
	private static boolean DEFAULT_VAL_INVERT = false;

	private static final int[] DEFAULT_AREA_RANGE = { 1000, 15000 };
	private static final double[] DEFAULT_ASPECT_RATIO_RANGE = { 0.6, 1.3 };

	private ColourSegmentor segmentor;

	private List<AreaOfInterest> signs;

	private Mat hsvImage;

	private int maxAreaInPixels;
	private int minAreaInPixels;

	private double maxAspectRatio;
	private double minAspectRatio;



	public SignDetector() {

	    segmentor = new ColourSegmentor();

		signs = new ArrayList<>();

		setDefaultProperties();

	}

	/* INITIALIZATION */

	private void setDefaultProperties() {
		segmentor.setChannelRange(HUE, DEFAULT_HUE_RANGE[RANGE_LOW], DEFAULT_HUE_RANGE[RANGE_HIGH]);
		segmentor.setChannelRange(SAT, DEFAULT_SAT_RANGE[RANGE_LOW], DEFAULT_SAT_RANGE[RANGE_HIGH]);
		segmentor.setChannelRange(VAL, DEFAULT_VAL_RANGE[RANGE_LOW], DEFAULT_VAL_RANGE[RANGE_HIGH]);
		segmentor.setInvertChannel(HUE, DEFAULT_HUE_INVERT);
		segmentor.setInvertChannel(SAT, DEFAULT_SAT_INVERT);
		segmentor.setInvertChannel(VAL, DEFAULT_VAL_INVERT);

		setMinAreaInPixels(DEFAULT_AREA_RANGE[RANGE_LOW]);
		setMaxAreaInPixels(DEFAULT_AREA_RANGE[RANGE_HIGH]);

		setMinAspectRatio(DEFAULT_ASPECT_RATIO_RANGE[RANGE_LOW]);
		setMaxAspectRatio(DEFAULT_ASPECT_RATIO_RANGE[RANGE_HIGH]);

	}

	public void init() {

		segmentor.init();

		CopilotConfig config = CopilotConfig.getInstance();

		hsvImage = new Mat(config.getFrameSize(), CvType.CV_8UC3);

	}
	
	

	/* FINDING AREAS OF INTEREST */

	@Override
	public List<AreaOfInterest> detect(Mat bgrImage) {

		Mat segmented = segment(bgrImage);

		Contours contours = findContours(segmented);
		return  getAreasOfInterest(bgrImage, contours);

	}

	public Mat segment(Mat bgrImage) {

		Imgproc.cvtColor(bgrImage, hsvImage, Imgproc.COLOR_BGR2HSV);

		return segmentor.segment(hsvImage);

	}

	public Contours findContours(Mat binaryImage) {
		Contours contours = new Contours();

		Imgproc.findContours(binaryImage, contours.getContours(), contours.getHierarchy(), Imgproc.RETR_TREE,
				Imgproc.CHAIN_APPROX_SIMPLE);

		return contours;
	}

	public List<AreaOfInterest> getAreasOfInterest(Mat bgrImage, Contours contours) {

		signs.clear();

		List<MatOfPoint> listOfContours = contours.getContours();

		int count = 1;
		int id = 0;

		for (MatOfPoint contour : listOfContours) {

		    Rect rect = Imgproc.boundingRect(contour);

		    double area = rect.area();
			double aspectRatio = (double) rect.height / (double) rect.width;

			if (isAreaOK(area) && isAspectRatioOK(aspectRatio)) {

			    AreaOfInterest sign = createAreaOfInterest(bgrImage, rect);
				sign.setId(id++);
				signs.add(sign);

			}

			count++;
		}

		return signs;
	}

	/* AREA TESTS */

	boolean isAreaOK(double area) {

		return (!isTooBig(area) && !isTooSmall(area));
	}

	boolean isAspectRatioOK(double aspectRatio) {
		return (!isTooWide(aspectRatio) && !(isTooTall(aspectRatio)));
	}

	boolean isTooBig(double area) {
		return area > getMaxAreaInPixels();
	}

	boolean isTooSmall(double area) {

		return area < getMinAreaInPixels();
	}

	boolean isTooWide(double aspectRatio) {

		return aspectRatio < getMinAspectRatio();
	}

	boolean isTooTall(double aspectRatio) {

		return aspectRatio > getMaxAspectRatio();
	}

	/* GETTERS AND SETTERS */

	int getMaxAreaInPixels() {

		return maxAreaInPixels;
	}

	private void setMaxAreaInPixels(int maxAreaInPixels) {

		this.maxAreaInPixels = maxAreaInPixels;
	}

	int getMinAreaInPixels() {

		return minAreaInPixels;
	}

	private void setMinAreaInPixels(int minAreaInPixels) {

		this.minAreaInPixels = minAreaInPixels;
	}

	double getMaxAspectRatio() {

		return maxAspectRatio;
	}

	private void setMaxAspectRatio(double maxAspectRatio) {

		this.maxAspectRatio = maxAspectRatio;
	}

	double getMinAspectRatio() {

		return minAspectRatio;
	}

	private void setMinAspectRatio(double minAspectRatio) {

		this.minAspectRatio = minAspectRatio;
	}

	ColourSegmentor getColourSegmentor() {

		return segmentor;
	}

	private AreaOfInterest createAreaOfInterest(Mat bgrImage, Rect rect) {
		Mat image = new Mat();
		bgrImage.submat(rect).copyTo(image);

		return new AreaOfInterest(signs.size(), image, rect);

	}

}
