package milesb.copilot.core.utils.hungarianalgorithm;

public class Edge {

    private Vertex vertexA;
    private Vertex  vertexB;
    private Link link;
    
	
    Edge(Vertex vertexA, Vertex vertexB, Link link) {
    	this.vertexA = vertexA;
    	this.vertexB = vertexB;
    	this.link = link;
    }
    
    public Link getLink() {
		return link;
	}

	Vertex getVertexA() {
		return vertexA;
	}

	Vertex getVertexB() {
		return vertexB;
	}

	public int getId() {
		return link.getId();
	}

	
}
