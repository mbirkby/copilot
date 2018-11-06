package milesb.copilot.core.common.processes;

import java.util.List;

import milesb.copilot.core.domain.AreaOfInterest;
import milesb.copilot.core.domain.TrackedObject;

public interface Tracker {
   
	List<TrackedObject> track (List<AreaOfInterest> areasOfInterest);
	void clearTrackedObjects();
	void init();
	
}
