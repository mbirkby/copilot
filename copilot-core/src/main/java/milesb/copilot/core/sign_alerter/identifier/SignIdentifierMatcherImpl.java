package milesb.copilot.core.sign_alerter.identifier;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorMatcher;


public class SignIdentifierMatcherImpl implements SignIdentifierMatcher {
	private static final double DEFAULT_THRESHOLD = 0.5;

	private double threshold;

	private List<Descriptor> descriptors;
	private DescriptorMatcher descriptorMatcher;

	public SignIdentifierMatcherImpl(List<Descriptor> descriptors) {
		this.descriptors = descriptors;
		this.threshold = DEFAULT_THRESHOLD;

	}

	public double getThreshold() {
		return threshold;
	}

	@Override
	public Descriptor match(Mat imageDescriptors) {

		Descriptor matchedDescriptor = null;

		MatchResult bestMatch = findBestMatch(imageDescriptors);

		if (bestMatch.score < threshold) {

			matchedDescriptor = bestMatch.descriptor;
		}

		return matchedDescriptor;
	}

	private MatchResult findBestMatch(Mat imageDescriptors) {

		MatchResult bestMatch = new MatchResult();
		bestMatch.descriptor = null;
		bestMatch.score = Double.MAX_VALUE;

		if (imageDescriptors.rows() > 0) {

			for (Descriptor descriptor : descriptors) {
				Mat currentDescriptor = descriptor.getDescriptor();

				double score = calcImageScore(imageDescriptors, currentDescriptor);

				if (bestMatch.descriptor == null || score <= bestMatch.score) {
					bestMatch.score = score;
					bestMatch.descriptor = descriptor;
				}

			}
		}

		return bestMatch;
	}

	private double calcImageScore(Mat imageDescriptors, Mat dbDescriptors) {
		double score = Double.MAX_VALUE;

		MatOfDMatch matches = new MatOfDMatch();

		descriptorMatcher.match(imageDescriptors, dbDescriptors, matches);

		List<DMatch> matchList = matches.toList();
		if (matchList.size() > 0) {
			score = 0;
			for (DMatch match : matchList) {
				score += match.distance;
			}

			score = score / matchList.size();
		}

		return score;

	}

	public void setDescriptorMatcher(DescriptorMatcher descriptorMatcher) {
		this.descriptorMatcher = descriptorMatcher;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public class MatchResult {
		public Descriptor descriptor;
		double score;
	}

}
