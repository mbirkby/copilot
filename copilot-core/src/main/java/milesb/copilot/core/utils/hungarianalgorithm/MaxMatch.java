package milesb.copilot.core.utils.hungarianalgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MaxMatch {
	private BipartiteGraph graph;

	private Vertex breakthroughVertex;
	
	

	enum Status {
		BREAKTHROUGH, BEST_MATCHING, MAX_MATCHING
	}

	private Status status;

	MaxMatch() {

		breakthroughVertex = null;
	}

	public BipartiteGraph getGraph() {
		return graph;

	}

	Status getStatus() {
		return status;
	}

	void setGraph(BipartiteGraph graph) {
		this.graph = graph;

	}

	private void initializeLabels() {
		initializeLabels(graph.getVerticesA());
		initializeLabels(graph.getVerticesB());
	}

	private void initializeLabels(Collection<Vertex> vertices) {
		for (Vertex vertex : vertices) {
			vertex.setLabel(null);
		}

	}

	Collection<Edge> findMaxMatching() {
		if (graph == null) {
			throw new MaxMatchException("No graph set.  Unable to perform MaxMatch algorithm");
		}

		status = null;

		while (!optimized()) {
			labellingProcedure();
			
			if (breakthrough()) {
				matchingImprovement();
				status = null;
			}
		}


		return getMatchings();

	}
	
	
	private void labellingProcedure() {

		
		initializeLabels();
		List<Vertex> startingVertices = labelStartingVertices();
		
		List<Vertex> labelledA = startingVertices;
		List<Vertex> labelledB;
		while(!breakthrough() && !optimized()) {
			labelledB = labelB(labelledA);
			
			if (!breakthrough()) {
			  labelledA = labelA(labelledB);
			}
		}
		
	}
	
	private boolean breakthrough() {
		return status == Status.BREAKTHROUGH;
	}
	
	private boolean optimized() {
		return status==Status.MAX_MATCHING || status==Status.BEST_MATCHING;
	}
	
	
	

	List<Vertex> labelStartingVertices() {

		boolean labelled = false;

		List<Vertex> startingVertices = new ArrayList<>();
		Collection<Vertex> verticesA = graph.getVerticesA();

		for (Vertex vertex : verticesA) {
			if (!hasConnectionsInMatching(vertex)) {
				vertex.setLabel(new Label());
				startingVertices.add(vertex);
				labelled = true;
			}
		}

		if (!labelled) {
			status = Status.MAX_MATCHING;
		}

		return startingVertices;
	}

	private boolean hasConnectionsInMatching(Vertex vertex) {
		boolean foundMatching = false;
		Iterator<Connection> i = vertex.getConnections().iterator();
		while (i.hasNext() && !foundMatching) {
			Connection connection = i.next();
			if (connection.getLink().isMatched()) {
				foundMatching = true;
			}

		}

		return foundMatching;
	}

	

	/**
	 * Loops through B vertices passed in. For each Vertex in B (vertexB), it sets
	 * the label to vertexB on all A vertices connected to by a link in the
	 * matchings and not already labelled
	 *
	 * 
	 * @param verticesB - List of Vertex in set B
	 * @return
	 */
	List<Vertex> labelA(List<Vertex> verticesB) {
		List<Vertex> verticesA = new ArrayList<>();
        boolean labelled = false;
		for (Vertex vertexB : verticesB) {
			Collection<Connection> connectionsB = vertexB.getConnections();
			for (Connection connectionB : connectionsB) {
				if (connectionB.getLink().isMatched()) {
					Vertex vertexA = connectionB.getVertex();
					if (!vertexA.hasLabel()) {
						vertexA.setLabel(new Label(vertexB));
						verticesA.add(vertexA);
						labelled = true;
					}
				}
			}

		}
		
		if (!labelled) {
			status = Status.BEST_MATCHING;
		}
		
		return verticesA;
	}

	


	/**
	 * Loops through A vertices passed in. For each Vertex in A (vertexA), it sets
	 * the label to vertexA on all B vertices connected to by a link that is not in
	 * the matchings and not already labelled. If the labelled B vertex does not
	 * have any connections in the matchings then it sets the status to BREAKTHROUGH
	 * and sets the breakthrough vertex; * @param verticesA
	 * 
	 * @return
	 */

	List<Vertex> labelB(List<Vertex> verticesA) {
		List<Vertex> verticesB = new ArrayList<>();

		boolean labelled = false;


		Iterator<Vertex> iterA = verticesA.iterator();
		while (iterA.hasNext() && status!=Status.BREAKTHROUGH) {
			Vertex vertexA = iterA.next();
			Iterator<Connection> connIter = vertexA.getConnections().iterator();
			while (connIter.hasNext() && status!=Status.BREAKTHROUGH) {
				Connection connectionToB = connIter.next();
				if (!connectionToB.getLink().isMatched()) {
					Vertex vertexB = connectionToB.getVertex();
					if (!vertexB.hasLabel()) {
						vertexB.setLabel(new Label(vertexA));
						verticesB.add(vertexB);
						labelled = true;
						if (!hasConnectionsInMatching(vertexB)) {
							breakthroughVertex = vertexB;
							status = Status.BREAKTHROUGH;
						}
					}
				}
			}

		}

		if (!labelled) {
			status = Status.MAX_MATCHING;
		}
		
		return verticesB;

	}



	Vertex getBreakthroughVertex() {
		return breakthroughVertex;
	}

	private void matchingImprovement() {
		List<Vertex> path = getAlternatingPath(breakthroughVertex);

		for (int i = 0; i < path.size() - 1; i++) {
			Vertex vertex = path.get(i);
			Connection conn = vertex.getConnectionTo(path.get(i + 1));
			Link link = conn.getLink();
			link.setMatched(!link.isMatched());
		}

	}

	private List<Vertex> getAlternatingPath(Vertex breakthrough) {
		List<Vertex> path = new ArrayList<>();
		Vertex vertex = breakthrough;
		while (vertex != null) {
			path.add(vertex);
			Label label = vertex.getLabel();
			vertex = label.getVertex();
		}

		return path;
	}

	private List<Edge> getMatchings() {
		Collection<Edge> edges = graph.getEdges();
		List<Edge> matchings = new ArrayList<>();

		for (Edge edge : edges) {
			Link link = edge.getLink();
			if (link.isMatched()) {
				matchings.add(edge);
			}
		}

		return matchings;
	}

}
