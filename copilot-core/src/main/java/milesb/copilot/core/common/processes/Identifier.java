package milesb.copilot.core.common.processes;

import java.util.List;

import milesb.copilot.core.domain.TrackedObject;

public interface Identifier {
	void identify(List<TrackedObject> trackedObjects);
	void init();
}
