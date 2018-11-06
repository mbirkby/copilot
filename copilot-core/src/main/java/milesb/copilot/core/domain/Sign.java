package milesb.copilot.core.domain;

import milesb.copilot.core.sign_alerter.identifier.SignType;

public class Sign extends TrackedObject {

	private SignType signType;

	public Sign() {
		super();
		signType = null;
	}

	public boolean isIdentified() {
		return signType != null;
	}

	public void setSignType(SignType signType) {
		this.signType = signType;
	}

	public SignType getSignType() {
		return signType;
	}

	@Override
	public String getLabel() {
		String label = super.getLabel();
		if (signType != null) {
			label = signType.getLabel();
		}
		return label;
	}

}
