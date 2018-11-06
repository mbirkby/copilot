package milesb.copilot.core.common.processes;

import milesb.copilot.core.domain.TrackedObject;

public interface Alerter {
	void soundAlert(TrackedObject trackedObjects);
	void init();
}
