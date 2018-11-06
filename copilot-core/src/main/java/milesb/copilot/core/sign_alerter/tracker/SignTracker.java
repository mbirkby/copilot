package milesb.copilot.core.sign_alerter.tracker;

import java.util.List;


import milesb.copilot.core.common.processes.AbstractTracker;
import milesb.copilot.core.common.processes.TrackerMatcher;
import milesb.copilot.core.domain.AreaOfInterest;
import milesb.copilot.core.domain.Sign;
import milesb.copilot.core.domain.TrackedObject;
import milesb.copilot.core.sign_alerter.tracker.matcher.Match;

/**
 * Each frame creates a new list of AreaOfInterest each representing a region of
 * the frame it thinks is occupied by a road sign. However to avoid having to
 * re-identify every AreaOfInterest on every frame, we try to identify the same
 * sign between frames from the first frame it appears in to the last frame as
 * the vehicle passes it. It is also necessary in order to work out if a new
 * sign has appeared (and should be alerted on)
 * 
 * SignTracker provides this functionality by maintaining a list of Sign objects
 * with each Sign object representing a single road sign being tracked from the
 * moment it appears to the moment it disappears out of view. It consists of the
 * following features
 * 
 * 1) Attempts to match each AreaOfInterest to an existing Sign
 * object(TrackedObject) 2) It should update signs that have matched with the
 * latest location/size and image details (so these can be matched later) 3)
 * Create and add new Signs objects for areas of interest not being matched
 * (setting the location\size and image details) 4) It should mark Sign objects
 * not matched in a given frame (either to not display them or display in
 * different colour) 5) It should remove Sign Objects that are no longer
 * relevant (i.e. gone permanently out of view) 6) It should mark new Sign
 * objects (so that an audible warning can be given for instance)
 * 
 * 
 * 
 * @author miles
 *
 */
public class SignTracker extends AbstractTracker {

	private static final double THRESHOLD_SCORE = Double.MAX_VALUE;

	private double thresholdScore;

	private TrackerMatcher matcher;

	public SignTracker(TrackerMatcher matcher) {
		this.matcher = matcher;
		thresholdScore = THRESHOLD_SCORE;

	}

	@Override
	public List<TrackedObject> track(List<AreaOfInterest> areasOfInterest) {

		beforeTracking();

		List<TrackedObject> trackedObjects = getTrackedObjects();

		List<Match> matches = matcher.match(areasOfInterest, trackedObjects);
		for (Match match : matches) {
			AreaOfInterest areaOfInterest = match.getAreaOfInterest();
			TrackedObject trackedObject = match.getTrackedObject();
			if (trackedObject == null || match.getScore() >= thresholdScore) {
				trackedObject = new Sign();
				addTrackedObject(trackedObject);
			} else {
				trackedObject.setMatchedInCurrentFrame(true);
			}

			trackedObject.setAreaOfInterest(areaOfInterest);

		}

		afterTracking();

		return trackedObjects;

	}

	@Override
	public void init() {

	}

	void setThresholdScore(double thresholdScore) {
		this.thresholdScore = thresholdScore;
	}
}
