package milesb.copilot.core;


import milesb.copilot.core.sign_alerter.SignFactory;

public interface CopilotFactory {
     SignFactory createSignFactory();
}
