package milesb.copilot.core;

import milesb.copilot.core.sign_alerter.SignFactory;
import milesb.copilot.core.sign_alerter.SignFactoryImpl;

public class DefaultCopilotFactory implements CopilotFactory {

	@Override
	public SignFactory createSignFactory() {	
		return new SignFactoryImpl();
	}

}
