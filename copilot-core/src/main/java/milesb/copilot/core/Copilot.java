package milesb.copilot.core;

import org.opencv.core.Mat;

import milesb.copilot.core.sign_alerter.SignAlerter;
import milesb.copilot.core.sign_alerter.SignFactory;

public class Copilot {


	private SignAlerter signAlerter;
	private int frameNum;

	public Copilot(CopilotFactory resourceFactory) {
		SignFactory signFactory = resourceFactory.createSignFactory();
		signAlerter = new SignAlerter(signFactory);
		frameNum = 0;


	}

	public SignAlerter getSignAlerter() {
		return signAlerter;
	}

	public Mat processFrame(Mat image) {
        frameNum++;

		signAlerter.execute(image);

		return image;

	}

	public void init() {
		signAlerter.init();
	}

}
