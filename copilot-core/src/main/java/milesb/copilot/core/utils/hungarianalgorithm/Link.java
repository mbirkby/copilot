package milesb.copilot.core.utils.hungarianalgorithm;

public class Link {
	private int id;
	private boolean matched;

	public Link(int id) {
		this.id = id;
		matched = false;
	}
	
	public int getId() {
		return id;
	}

	public Link(int id, boolean matched) {
		this.id = id;
		this.matched = false;
	}
	
	
	
	boolean isMatched() {
		return matched;
	}

	void setMatched(boolean matched) {
		this.matched = matched;
	}

}
