package milesb.copilot.core.sign_alerter.identifier;

public class Label {

	private int signTypeId;
	private String text;

	public Label() {

	}

	public Label(int signTypeId, String text) {
		super();
		this.signTypeId = signTypeId;
		this.text = text;
	}

	public int getSignTypeId() {
		return signTypeId;
	}

	public void setSignTypeId(int signTypeId) {
		this.signTypeId = signTypeId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
