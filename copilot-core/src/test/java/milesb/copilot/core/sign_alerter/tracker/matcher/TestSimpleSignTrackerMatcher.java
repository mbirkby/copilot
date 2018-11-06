package milesb.copilot.core.sign_alerter.tracker.matcher;

import static org.junit.Assert.*;

import java.util.ArrayList;

import java.util.List;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import milesb.copilot.core.common.processes.TrackerScorer;
import milesb.copilot.core.domain.AreaOfInterest;
import milesb.copilot.core.domain.ObjectUtils;
import milesb.copilot.core.domain.Sign;
import milesb.copilot.core.domain.TrackedObject;
import milesb.copilot.opencv.OpenCVLibrary;

public class TestSimpleSignTrackerMatcher {

	private List<AreaOfInterest> aoiList;
	private List<TrackedObject> toList;

	private SimpleSignTrackerMatcher matcher;
	private TrackerScorer trackerScorer;

	@BeforeClass
	public static void init() {
		OpenCVLibrary.loadLibrary();
	}

	@Before
	public void setup() {

		trackerScorer = mock(TrackerScorer.class);
		matcher = new SimpleSignTrackerMatcher(trackerScorer);

		aoiList = new ArrayList<>();
		toList = new ArrayList<>();

	}

	@Test
	public void testSingleMatch() {

		aoiList.add(new AreaOfInterest(0, cross(), new Point(100, 100)));
		toList.add(createSign(0, cross(), new Point(100, 100)));

		when(trackerScorer.calcScores(aoiList, toList)).thenReturn(new double[][] { { 0.0 } });
		List<Match> matches = matcher.match(aoiList, toList);

		assertEquals(0, matches.get(0).getAreaOfInterest().getId());
		assertNotNull(matches.get(0).getTrackedObject());
		assertEquals("Incorrect score - should be zero for identical images in same loaction", 0.0,
				matches.get(0).getScore(), 0.01);

	}

	@Test
	public void testWhenMoreAOIsThanTrackedObjects() {
		aoiList.add(new AreaOfInterest(0, cross(), new Point(50, 50)));
		aoiList.add(new AreaOfInterest(1, plus(), new Point(350, 50)));

		toList.add(createSign(0, plus(), new Point(200, 200)));

		when(trackerScorer.calcScores(aoiList, toList)).thenReturn(new double[][] { { 5.0 }, { 2.0 } });
		List<Match> matches = matcher.match(aoiList, toList);
		assertEquals("Incorrect number of matches returned", 2, matches.size());

		Match m0 = matches.get(0);
		if (m0.getAreaOfInterest().getId() == 0) {
			assertNull("aoi[0] not should be matched as it has a higher score", m0.getTrackedObject());
		} else if (m0.getAreaOfInterest().getId() == 1) {
			assertEquals("aoi should be matched to to0", 0, m0.getTrackedObject().getId());
		}

		Match m1 = matches.get(1);

		if (m1.getAreaOfInterest().getId() == 0) {
			assertNull("aoi[0] not should be matched as it has a higher score", m1.getTrackedObject());
		} else if (m1.getAreaOfInterest().getId() == 1) {
			assertEquals("aoi should be matched to to0", 0, m1.getTrackedObject().getId());
		}
	}

	@Test
	public void testWhenNoTrackedObjects() {
		aoiList.add(new AreaOfInterest(0, cross(), new Point(50, 50)));
		aoiList.add(new AreaOfInterest(1, plus(), new Point(350, 50)));

		when(trackerScorer.calcScores(aoiList, toList)).thenReturn(new double[][] { {}, {} });
		List<Match> matches = matcher.match(aoiList, toList);
		assertEquals("Incorrect number of matches returned", 2, matches.size());

		assertNull("match 0 should be null", matches.get(0).getTrackedObject());
		assertNull("match 1 should be null", matches.get(1).getTrackedObject());

	}

	@Test
	public void testWhenMoreTrackedObjectsThanAOIs() {
		aoiList.add(new AreaOfInterest(0, cross(), new Point(200, 150)));
		toList.add(createSign(0, cross(), new Point(50, 200)));
		toList.add(createSign(1, cross(), new Point(200, 200)));

		when(trackerScorer.calcScores(aoiList, toList)).thenReturn(new double[][] { { 3.0, 1.0 } });
		List<Match> matches = matcher.match(aoiList, toList);
		Match match = matches.get(0);
		assertEquals("Should match to the 2nd tracked object", match.getTrackedObject(), toList.get(1));
	}

	private Sign createSign(int id, Mat image, Point centreLocation) {
		Sign sign = new Sign();
		sign.setId(id);
		Rect boundingRect = ObjectUtils.centrePointToRect(centreLocation, image.size());
		sign.setBoundingRect(boundingRect);

		return sign;

	}

	private Mat plus() {
		Mat redPlus = new Mat(100, 80, CvType.CV_8UC3);
		Core.line(redPlus, new Point(0, 40), new Point(99, 40), new Scalar(0, 0, 255), 2);
		Core.line(redPlus, new Point(50, 0), new Point(50, 99), new Scalar(0, 0, 255), 2);

		return redPlus;
	}

	private Mat cross() {
		Mat redCross = new Mat(100, 100, CvType.CV_8UC3);

		Core.line(redCross, new Point(0, 0), new Point(99, 99), new Scalar(0, 0, 255), 2);
		Core.line(redCross, new Point(99, 0), new Point(0, 99), new Scalar(0, 0, 255), 2);

		return redCross;
	}

}
