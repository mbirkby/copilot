package milesb.copilot.core.common.processes;

import java.util.List;

import milesb.copilot.core.domain.AreaOfInterest;
import milesb.copilot.core.domain.TrackedObject;

public interface TrackerScorer {
	double[][] calcScores(List<AreaOfInterest> areasOfInterest, List<TrackedObject> trackedObjects);
	void init();
}
