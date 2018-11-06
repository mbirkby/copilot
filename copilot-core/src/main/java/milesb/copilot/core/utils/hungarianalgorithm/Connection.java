package milesb.copilot.core.utils.hungarianalgorithm;

public class Connection {
	private Link link;
	private Vertex vertex;

	Connection(Link link, Vertex vertex) {
		this.link = link;
		this.vertex = vertex;
	}

	public Link getLink() {
		return link;
	}

	public Vertex getVertex() {
		return vertex;
	}
}
