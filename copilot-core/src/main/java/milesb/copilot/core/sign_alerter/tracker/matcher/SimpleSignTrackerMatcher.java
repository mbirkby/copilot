package milesb.copilot.core.sign_alerter.tracker.matcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import milesb.copilot.core.common.processes.TrackerMatcher;
import milesb.copilot.core.common.processes.TrackerScorer;
import milesb.copilot.core.domain.AreaOfInterest;
import milesb.copilot.core.domain.TrackedObject;

/**
 * Attempts to match each AreaOfInterest passed in to a trackedObject from the
 * passed in tracked objects list. Returns a list of Match - one match per
 * AreaOfInterest although it might not actually link to a TrackedObject
 * 
 * @author miles
 *
 */
public class SimpleSignTrackerMatcher implements TrackerMatcher {

	private TrackerScorer trackerScorer;

	public SimpleSignTrackerMatcher(TrackerScorer trackerScorer) {
		this.trackerScorer = trackerScorer;
	}

	@Override
	public List<Match> match(List<AreaOfInterest> areasOfInterest, List<TrackedObject> trackedObjects) {

		double[][] scores = trackerScorer.calcScores(areasOfInterest, trackedObjects);
		return getMatches(areasOfInterest, trackedObjects, scores);


	}

	@Override
	public void init() {

	}

	/**
	 * Creates a list of Match objects with an Match entry for each AreaOfInterest
	 * passed in. The best scoring match is include in the entry as is the score.
	 * 
	 * Note: this will not always produce the optimum combination of matches ie
	 * (lowest total score) but should match most areas of interest to the nearest
	 * (lowest score) tracked object
	 * 
	 * @param areasOfInterest
	 * @param trackedObjects
	 * @param scores
	 */

	private List<Match> getMatches(List<AreaOfInterest> areasOfInterest, List<TrackedObject> trackedObjects,
								   double[][] scores) {

		boolean[] assignedAreasOfInterest = new boolean[areasOfInterest.size()];
		boolean[] assignedTrackedObjects = new boolean[trackedObjects.size()];
		List<Match> matches = new ArrayList<>();
		List<Edge> sortedEdges = sortEdges(scores);
		for (Edge edge : sortedEdges) {
			if (!assignedAreasOfInterest[edge.getAreaOfInterestIndex()]) {
				Match match;
				if (!assignedTrackedObjects[edge.getTrackedObjectIndex()]) {
					match = new Match(areasOfInterest.get(edge.getAreaOfInterestIndex()),
							trackedObjects.get(edge.getTrackedObjectIndex()), edge.getScore());
					assignedTrackedObjects[edge.getTrackedObjectIndex()] = true;
					assignedAreasOfInterest[edge.getAreaOfInterestIndex()] = true;
					matches.add(match);
				}

			}
		}

		// fill in unmatched
		for (int i = 0; i < assignedAreasOfInterest.length; i++) {
			if (!assignedAreasOfInterest[i]) {
				Match match = new Match(areasOfInterest.get(i), null, Double.MAX_VALUE);
				matches.add(match);
			}
		}

		return matches;

	}

	private List<Edge> sortEdges(double[][] scores) {
		List<Edge> sortedEdges = new ArrayList<>();

		for (int aoi = 0; aoi < scores.length; aoi++) {
			for (int to = 0; to < scores[aoi].length; to++) {
				Edge edge = new Edge(scores[aoi][to], new Integer[] { aoi, to });
				sortedEdges.add(edge);
			}
		}

		Collections.sort(sortedEdges);

		return sortedEdges;

	}

	public class Edge implements Comparable<Edge> {
		private double score;
		private Integer[] link;

		Edge(double score, Integer[] link) {
			this.score = score;
			this.link = link;
		}

		int getAreaOfInterestIndex() {
			return link[0];
		}

		int getTrackedObjectIndex() {
			return link[1];
		}

		double getScore() {
			return score;
		}

		@Override
		public int compareTo(Edge o) {
			return (int) (score - o.score);
		}
	}

	public void setTrackerScorer(TrackerScorer trackerScorer) {
		this.trackerScorer = trackerScorer;
	}

}
