package milesb.copilot.core.sign_alerter.alerter;

import milesb.copilot.core.common.AudioResources;
import milesb.copilot.core.common.processes.Alerter;
import milesb.copilot.core.domain.TrackedObject;

public class SignAudioAlerter implements Alerter {
	private AudioResources audioResources;

	public SignAudioAlerter(AudioResources audioResources) {
		this.audioResources = audioResources;
	}

	@Override
	public void soundAlert(TrackedObject trackedObjects) {
		audioResources.beep();

	}

	@Override
	public void init() {

	}

}
