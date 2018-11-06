package milesb.copilot.core.sign_alerter;

import java.util.List;

import org.opencv.core.Mat;

import milesb.copilot.core.common.processes.Alerter;
import milesb.copilot.core.common.processes.Detector;
import milesb.copilot.core.common.processes.Identifier;
import milesb.copilot.core.common.processes.Renderer;
import milesb.copilot.core.common.processes.Tracker;
import milesb.copilot.core.domain.AreaOfInterest;
import milesb.copilot.core.domain.TrackedObject;

public class SignAlerter {
	private Detector detector;
	private Tracker tracker;
	private Identifier identifier;
	private Renderer renderer;
	private Alerter alerter;

	public SignAlerter(SignFactory signFactory) {
		detector = signFactory.createDetector();
		tracker = signFactory.createTracker();
		identifier = signFactory.createIdentifier();
		renderer = signFactory.createRenderer();
		alerter = signFactory.createAudioAlerter();
	}

	public void execute(Mat frame) {

		try {
			List<TrackedObject> signs = findSigns(frame);
			renderSigns(frame, signs);
			soundAlerts(signs);
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void init() {
		detector.init();
		tracker.init();
		identifier.init();
		renderer.init();
		alerter.init();
	}

	private List<TrackedObject> findSigns(Mat frame) {

		List<AreaOfInterest> signAreas = detect(frame);

		List<TrackedObject> signs = track(signAreas);
		identify(signs);

		return signs;
	}

	private List<AreaOfInterest> detect(Mat frame) {

		return detector.detect(frame);

	}

	private List<TrackedObject> track(List<AreaOfInterest> signs) {
		return tracker.track(signs);

	}

	public void clearTrackedObjects() {
		tracker.clearTrackedObjects();
	}

	private void identify(List<TrackedObject> signs) {
		identifier.identify(signs);
	}

	private void renderSigns(Mat image, List<TrackedObject> signs) {
		for (TrackedObject trackedObject : signs) {
			renderer.render(image, trackedObject);
		}
	}

	private void soundAlerts(List<TrackedObject> signs) {
		for (TrackedObject sign : signs) {
			if (sign.hasAlert()) {
				alerter.soundAlert(sign);
			}
		}

	}

}
