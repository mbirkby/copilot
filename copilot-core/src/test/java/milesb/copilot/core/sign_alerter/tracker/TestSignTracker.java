package milesb.copilot.core.sign_alerter.tracker;

import static org.junit.Assert.*;

import java.util.ArrayList;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import org.opencv.core.Size;

import milesb.copilot.core.common.processes.TrackerMatcher;
import milesb.copilot.core.domain.AreaOfInterest;
import milesb.copilot.core.domain.ObjectUtils;
import milesb.copilot.core.domain.Sign;
import milesb.copilot.core.domain.TrackedObject;
import milesb.copilot.core.sign_alerter.tracker.matcher.Match;
import milesb.copilot.opencv.OpenCVLibrary;
import milesb.copilot.test.utils.ImageCreators;

/**
 * Notes:
 * 
 * Uses a mock object for the actual matcher but be aware that the
 * implementation of this is required to create a Match object for every
 * AreaOfInterest passed in. This should be replicated in the testing by making
 * sure there is a Match object in matchList for every AreaOfInterest in the
 * aoiList
 * 
 * @author miles
 *
 */

public class TestSignTracker {

	private SignTracker signTracker;
	private TrackerMatcher matcher;

	private List<AreaOfInterest> aoiList;
	private List<Match> matchList;
	private List<TrackedObject> toList;

	@BeforeClass
	public static void init() {
		OpenCVLibrary.loadLibrary();
	}

	@Before
	public void setup() {
		matcher = mock(TrackerMatcher.class);
		signTracker = new SignTracker(matcher);
		aoiList = new ArrayList<>();
		matchList = new ArrayList<>();
		when(matcher.match(aoiList, signTracker.getTrackedObjects())).thenReturn(matchList);
		toList = signTracker.getTrackedObjects();
	}

	@Test
	public void testUnmatchedAreaOfInterestCreatesNewTrackedObject() {
		// setup
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 150)));
		matchList.add(new Match(aoiList.get(0), null, Double.MAX_VALUE));

		// run
		List<TrackedObject> signs = signTracker.track(aoiList);

		// test
		assertEquals(1, signs.size());
	}

	@Test
	public void testPartiallyMatchedAreaOfInterestCreatesNewTrackedObject() {
		// setup
		signTracker.setThresholdScore(10);
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 150)));

		Sign sign1 = new Sign();
		signTracker.addTrackedObject(sign1);

		matchList.add(new Match(aoiList.get(0), sign1, 10));

		// run
		List<TrackedObject> signs = signTracker.track(aoiList);

		// test
		assertEquals(2, signs.size());
	}

	@Test
	public void testMatchedAreaOfInterestDoesNotCreateNewTrackedObject() {
		signTracker.setThresholdScore(10);
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 150)));

		Sign sign1 = new Sign();
		signTracker.addTrackedObject(sign1);

		matchList.add(new Match(aoiList.get(0), sign1, 9));

		List<TrackedObject> signs = signTracker.track(aoiList);

		assertEquals(1, signs.size());
	}

	@Test
	public void testMatchedInFrameFlagSetWhenMatched() {
		// setup
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 150)));

		Sign sign = new Sign();
		signTracker.addTrackedObject(sign);

		matchList.add(new Match(aoiList.get(0), sign, 0));

		// run
		signTracker.track(aoiList);

		// test
		assertTrue(sign.isMatchedInCurrentFrame());
	}

	@Test
	public void testMatchInFrameUnsetWhenUnmatched() {
		// setup
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 150)));
		Sign sign = new Sign();
		signTracker.addTrackedObject(sign);
		matchList.add(new Match(aoiList.get(0), sign, 0));

		// run with match
		signTracker.track(aoiList);// should be matched at this point

		// run with no match between aoi and sign
		matchList.clear();
		matchList.add(new Match(aoiList.get(0), null, 0));
		signTracker.track(aoiList);// have failed to match existing sign

		// test
		assertFalse(sign.isMatchedInCurrentFrame());

	}

	@Test
	public void testNewTrackedObjectSetWhenCreated() {
		// setup
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 150)));
		matchList.add(new Match(aoiList.get(0), null, Double.MAX_VALUE));

		// run
		List<TrackedObject> signs = signTracker.track(aoiList);

		// test
		assertTrue(signs.get(0).isNewTrackedObject());

	}

	@Test
	public void testNewTrackedObjectUnsetInNextFrame() {
		// setup
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 150)));
		matchList.add(new Match(aoiList.get(0), null, Double.MAX_VALUE));

		List<TrackedObject> signs = signTracker.track(aoiList);

		// test
		assertFalse("Should be set as false after second frame", signs.get(0).isNewTrackedObject());
	}

	@Test
	public void testNewSignHasZeroUnmatchedCount() {
		// setup
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 150)));
		matchList.add(new Match(aoiList.get(0), null, Double.MAX_VALUE));

		// run
		List<TrackedObject> signs = signTracker.track(aoiList);

		// test
		assertEquals(0, signs.get(0).getNumFramesUnmatched());

	}

	@Test
	public void testUnMatchedSignIncrementsUnmatchedCount() {
		// setup
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 150)));
		Sign sign = new Sign();
		signTracker.addTrackedObject(sign);

		matchList.add(new Match(aoiList.get(0), null, Double.MAX_VALUE));

		// run
		signTracker.track(aoiList);

		// test
		assertEquals(1, sign.getNumFramesUnmatched());
	}

	@Test
	public void testMatchedSignResetsUnmatchedCount() {
		// setup
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 150)));
		Sign sign = new Sign();
		signTracker.addTrackedObject(sign);

		matchList.add(new Match(aoiList.get(0), null, Double.MAX_VALUE));

		// run
		signTracker.track(aoiList);// should increment count on sign to 1

		matchList.clear();
		matchList.add(new Match(aoiList.get(0), sign, 0));
		signTracker.track(aoiList);// sign is matched in this frame so should clear count

		assertEquals(0, sign.getNumFramesUnmatched());

	}

	@Test
	public void testSignsRemovedWhenUnmatchedCountReachesThreshold() {
		// setup
		signTracker.setNumUnmatchedFramesBeforeDelete(3);

		Sign sign = new Sign();
		signTracker.addTrackedObject(sign);

		// Sign should be unmatched 3 times and after the third should be deleted
		signTracker.track(aoiList);
		signTracker.track(aoiList);
		signTracker.track(aoiList);

		assertEquals("Sign should have been removed", 0, signTracker.getTrackedObjects().size());
	}

	@Test
	public void testWhenNoAreasOfInterestNoTrackedObjectsCreates() {
		List<TrackedObject> signs = signTracker.track(aoiList);

		assertEquals(0, signs.size());
	}

	@Test
	public void testMatchedTrackedObjectImageUpdated() {
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 180)));
		Sign sign = new Sign();
		matchList.add(new Match(aoiList.get(0), sign, 0));

		signTracker.track(aoiList);

		Mat signImage = sign.getImage();
		assertNotNull("No image set", signImage);
		assertEquals("Incorrect size - image has not been updated", new Size(100, 120), signImage.size());
	}

	@Test
	public void testMatchedTrackedObjectLocationUpdated() {
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 180)));

		Sign sign = new Sign();
		matchList.add(new Match(aoiList.get(0), sign, 0));

		signTracker.track(aoiList);

		Point centreLocation = sign.getCentreLocation();
		assertNotNull("No centre loaction set", centreLocation);

		assertEquals("Incorrect x coordinate of sign", 150.0, centreLocation.x, 0.05);
		assertEquals("Incorrect y coordinate of sign", 180.0, centreLocation.y, 0.05);

	}

	@Test
	public void testMatchedTrackedObjectDimensionsUpdated() {
		aoiList.add(new AreaOfInterest(0, ImageCreators.cross(), new Point(150, 180)));

		Sign sign = new Sign();
		matchList.add(new Match(aoiList.get(0), sign, 0));

		signTracker.track(aoiList);

		Rect boundingRect = sign.getBoundingRect();
		assertEquals("Incorrect width of sign", 100.0, boundingRect.width, 0.05);
		assertEquals("Incorrect height of sign", 120.0, boundingRect.height, 0.05);

	}

	public Sign createSign(Mat image, Point centreLocation) {
		Sign sign = new Sign();
		Rect boundingRect = ObjectUtils.centrePointToRect(centreLocation, image.size());
		sign.setBoundingRect(boundingRect);

		return sign;

	}

}
