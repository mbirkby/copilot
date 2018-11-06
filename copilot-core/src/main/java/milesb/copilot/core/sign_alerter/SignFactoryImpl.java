package milesb.copilot.core.sign_alerter;

import java.util.List;

import org.opencv.features2d.DescriptorMatcher;

import milesb.copilot.core.CopilotConfig;
import milesb.copilot.core.SystemResources;
import milesb.copilot.core.common.AudioResources;

import milesb.copilot.core.common.processes.Detector;
import milesb.copilot.core.common.processes.Tracker;
import milesb.copilot.core.common.processes.Identifier;
import milesb.copilot.core.common.processes.Renderer;
import milesb.copilot.core.common.processes.Alerter;
import milesb.copilot.core.common.processes.TrackerMatcher;
import milesb.copilot.core.common.processes.TrackerScorer;
import milesb.copilot.core.sign_alerter.detector.SignDetector;
import milesb.copilot.core.sign_alerter.tracker.SignTracker;
import milesb.copilot.core.sign_alerter.tracker.matcher.SimpleSignTrackerMatcher;
import milesb.copilot.core.sign_alerter.identifier.Descriptor;
import milesb.copilot.core.sign_alerter.identifier.DescriptorGenerator;
import milesb.copilot.core.sign_alerter.identifier.SignDatabase;
import milesb.copilot.core.sign_alerter.identifier.SignIdentifier;
import milesb.copilot.core.sign_alerter.identifier.SignIdentifierMatcher;
import milesb.copilot.core.sign_alerter.identifier.SignIdentifierMatcherImpl;
import milesb.copilot.core.sign_alerter.identifier.data.FileSignDatabase;
import milesb.copilot.core.sign_alerter.renderer.SignRenderer;
import milesb.copilot.core.sign_alerter.alerter.SignAudioAlerter;

import milesb.copilot.core.sign_alerter.tracker.scorer.SignTrackerScorerImpl;

public class SignFactoryImpl implements SignFactory {

	private SignDatabase signDatabase;

	public SignFactoryImpl() {

	}

	public Detector createDetector() {
		return new SignDetector();
	}

	public Tracker createTracker() {
		TrackerMatcher matcher = createTrackerMatcher();
		return new SignTracker(matcher);
	}

	public Identifier createIdentifier() {

		SignIdentifier signIdentifier = new SignIdentifier(getSignDatabase());

		SignIdentifierMatcher signMatcher = createSignMatcher();

		signIdentifier.setSignMatcher(signMatcher);

		DescriptorGenerator descriptorGenerator = signDatabase.getDescriptorGenerator();
		signIdentifier.setDescriptorGenerator(descriptorGenerator);

		return signIdentifier;
	}

	public Renderer createRenderer() {
		return new SignRenderer();
	}

	public Alerter createAudioAlerter() {
		SystemResources systemResources = CopilotConfig.getInstance().getSystemResources();
		AudioResources audioOutput = systemResources.getAudioResources();

		return new SignAudioAlerter(audioOutput);
	}

	private SignDatabase getSignDatabase() {
		if (signDatabase == null) {
			signDatabase = createSignDatabase();
		}

		return signDatabase;
	}

	private SignDatabase createSignDatabase() {
		SystemResources systemResources = CopilotConfig.getInstance().getSystemResources();
		return new FileSignDatabase(systemResources.getFileResources());
	}

	private SignIdentifierMatcher createSignMatcher() {

		List<Descriptor> descriptors = getSignDatabase().getDescriptors();
		SignIdentifierMatcherImpl matcher = new SignIdentifierMatcherImpl(descriptors);
		DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
		matcher.setDescriptorMatcher(descriptorMatcher);

		return matcher;
	}

	private TrackerMatcher createTrackerMatcher() {
		TrackerScorer trackerScorer = createTrackerScorer();

		return new SimpleSignTrackerMatcher(trackerScorer);

	}

	private TrackerScorer createTrackerScorer() {
		return new SignTrackerScorerImpl();
	}

}
