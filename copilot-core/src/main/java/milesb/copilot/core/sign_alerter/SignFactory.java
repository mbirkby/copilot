package milesb.copilot.core.sign_alerter;


import milesb.copilot.core.common.processes.Alerter;
import milesb.copilot.core.common.processes.Detector;
import milesb.copilot.core.common.processes.Identifier;
import milesb.copilot.core.common.processes.Renderer;
import milesb.copilot.core.common.processes.Tracker;

public interface SignFactory {
    Detector createDetector();
    Tracker createTracker();
    Identifier createIdentifier();
    Renderer createRenderer();
    Alerter createAudioAlerter();
 
}
