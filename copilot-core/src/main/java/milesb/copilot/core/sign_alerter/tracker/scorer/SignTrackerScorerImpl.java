package milesb.copilot.core.sign_alerter.tracker.scorer;

import java.util.List;

import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import milesb.copilot.core.common.processes.TrackerScorer;
import milesb.copilot.core.domain.AreaOfInterest;
import milesb.copilot.core.domain.Sign;
import milesb.copilot.core.domain.TrackedObject;

/**
 * Calculates the scores (distances) between every area of interest and every tracked object - low score means
 * the area of interest is a better match to the tracked object
 * 
 * @author miles
 *
 */
public class SignTrackerScorerImpl implements TrackerScorer{
   private double[][] scores;
   
   public double[][] calcScores(List<AreaOfInterest> areasOfInterest, List<TrackedObject> trackedObjects) {

		scores = new double[areasOfInterest.size()][trackedObjects.size()];

		for (int areaIndex = 0; areaIndex < areasOfInterest.size(); areaIndex++) {
			AreaOfInterest areaOfInterest = areasOfInterest.get(areaIndex);

			for (int signIndex = 0; signIndex < trackedObjects.size(); signIndex++) {
				Sign sign = (Sign) trackedObjects.get(signIndex);
				scores[areaIndex][signIndex] = calcScore(areaOfInterest, sign);

			}
		}

		return scores;
	}

	@Override
	public void init() {

	}


	private double calcScore(AreaOfInterest areaOfInterest, Sign sign) {
		double positionScore = calcPositionScore(areaOfInterest, sign);
		double aspectRatioScore = calcAspectRatioScore(sign);

		return positionScore + aspectRatioScore;

	}

	private double calcAspectRatioScore(Sign sign) {
		Size size = sign.getSize();
		double aspectRatio = size.height / size.width;

		return  Math.pow(aspectRatio - 1, 2);

	}

	private double calcPositionScore(AreaOfInterest areaOfInterest, Sign sign) {

		Point aoiCentre = areaOfInterest.getCentreLocation();
		Point signCentre = sign.getCentreLocation();

		return Math.pow(aoiCentre.x - signCentre.x, 2) + Math.pow(aoiCentre.y - signCentre.y, 2);

	}

	public double calcSizeScore(AreaOfInterest areaOfInterest, Sign sign) {
		Rect aoiRect = areaOfInterest.getBoundingRect();
		Rect signRect = sign.getBoundingRect();

		return  Math.pow(aoiRect.width - signRect.width, 2) + Math.pow(aoiRect.height - signRect.height, 2);

	}

}
