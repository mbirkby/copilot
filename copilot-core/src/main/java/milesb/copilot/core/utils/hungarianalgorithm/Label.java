package milesb.copilot.core.utils.hungarianalgorithm;

public class Label {
	private Vertex vertex;

	Label() {
		vertex = null;
	}
	
	Label(Vertex vertex) {
		this.vertex = vertex;
	}

	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}

	public Vertex getVertex() {
		return vertex;
	}
}
