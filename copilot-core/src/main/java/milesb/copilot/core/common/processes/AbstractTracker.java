package milesb.copilot.core.common.processes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import milesb.copilot.core.domain.TrackedObject;

public abstract class AbstractTracker implements Tracker {
	private static int id = 0;

	private static final int DEFAULT_UNMATCHED_FRAMES_BEFORE_DELETE = 20;

	private List<TrackedObject> trackedObjects;

	private int numUnmatchedFramesBeforeDelete;

	protected AbstractTracker() {
		trackedObjects = new ArrayList<>();
		numUnmatchedFramesBeforeDelete = DEFAULT_UNMATCHED_FRAMES_BEFORE_DELETE;
	}

	public void addTrackedObject(TrackedObject trackedObject) {

		trackedObject.setId(nextId());
		trackedObjects.add(trackedObject);
	}

	public List<TrackedObject> getTrackedObjects() {
		return Collections.unmodifiableList(trackedObjects);
	}

	/**
	 * Initializes tracked objects prior to generating new matches
	 * 
	 */
	protected void beforeTracking() {

		for (TrackedObject to : trackedObjects) {
			to.setMatchedInCurrentFrame(false);
			to.setNewTrackedObject(false);
		}

	}

	protected void afterTracking() {

		updateNumFramesUnmatched();
		removeOldTrackedObjects();

	}

	private void updateNumFramesUnmatched() {

		for (TrackedObject to : trackedObjects) {
			if (to.isMatchedInCurrentFrame()) {
				to.resetNumFramesUnmatched();
			} else {
				to.incrementNumFramesUnmatched();
			}
		}
	}

	private void removeOldTrackedObjects() {

		Iterator<TrackedObject> iter = trackedObjects.iterator();
		while (iter.hasNext()) {

			TrackedObject to = iter.next();
			if (to.getNumFramesUnmatched() >= numUnmatchedFramesBeforeDelete) {

				iter.remove();
			}
		}
	}

	public void clearTrackedObjects() {

		trackedObjects.clear();
	}

	public int getNumUnmatchedFramesBeforeDelete() {

		return numUnmatchedFramesBeforeDelete;
	}

	public void setNumUnmatchedFramesBeforeDelete(int numUnmatchedFramesBeforeDelete) {
		this.numUnmatchedFramesBeforeDelete = numUnmatchedFramesBeforeDelete;
	}

	private int nextId() {
		id++;
		return id;
	}

}
