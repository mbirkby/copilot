package milesb.copilot.core.sign_alerter.tracker.matcher;

import milesb.copilot.core.domain.AreaOfInterest;
import milesb.copilot.core.domain.TrackedObject;

public class Match {
	private AreaOfInterest areaOfInterest;
	private TrackedObject trackedObject;
	private double score;

	public AreaOfInterest getAreaOfInterest() {
		return areaOfInterest;
	}

	public TrackedObject getTrackedObject() {
		return trackedObject;
	}

	public double getScore() {
		return score;
	}

	public Match(AreaOfInterest areaOffInterest, TrackedObject trackedObject, double score) {
		super();
		this.areaOfInterest = areaOffInterest;
		this.trackedObject = trackedObject;
		this.score = score;
	}
}
