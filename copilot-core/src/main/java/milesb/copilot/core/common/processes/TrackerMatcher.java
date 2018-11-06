package milesb.copilot.core.common.processes;

import java.util.List;

import milesb.copilot.core.domain.AreaOfInterest;
import milesb.copilot.core.domain.TrackedObject;
import milesb.copilot.core.sign_alerter.tracker.matcher.Match;

public interface TrackerMatcher {
    List<Match> match(List<AreaOfInterest> areasOfInterest, List<TrackedObject> trackedObjects);
    void init();
    
}
