package milesb.copilot.core.utils.hungarianalgorithm;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import milesb.copilot.core.domain.InvalidAreaOfInterestException;

public class TestMaxMatch {

	private MaxMatch maxMatch;
	private BipartiteGraph graph;
    	
	
	@Before
	public void setup() {
		maxMatch = new MaxMatch();
		graph = new BipartiteGraph();
	}
	
	
	@Test (expected = MaxMatchException.class)
	public void testFindMaxMatchingWithNoGraphReturnsMaxMatchException() {		
		
		Collection<Edge> matches = maxMatch.findMaxMatching();
		
	}
	
	@Test
	public void testFindMaxMatchingWithFullMatching() {
		buildGraph1();
		maxMatch.setGraph(graph);
		
		Collection<Edge> matches = maxMatch.findMaxMatching();
		
		assertEquals(4, matches.size());
		
		

	}
	
	@Test
	public void testLabelStartingVertices() {
		buildGraph1();
		maxMatch.setGraph(graph);
		
		
		List<Vertex> startingLabels = maxMatch.labelStartingVertices();
		
		assertEquals("Should return 1 starting label",1,startingLabels.size());
		assertEquals("Starting label should be 3",3, startingLabels.get(0).getId());
		
	}
	
	@Test
	public void testLabelB() {
		buildGraph1();
		maxMatch.setGraph(graph);
		
		List<Vertex> verticesA = new ArrayList<>();
		verticesA.add(graph.getVertexA(3));
		
		List<Vertex> verticesB = maxMatch.labelB(verticesA);
		
		assertEquals("Should label 3 vertices",3,verticesB.size());
		assertTrue("Should contain vertex 1 in B",verticesB.contains(new Vertex(1)));
		assertTrue("Should contain vertex 2 in B",verticesB.contains(new Vertex(2)));
		assertTrue("Should contain vertex 4 in B",verticesB.contains(new Vertex(4)));
		
	}
	
	@Test
	public void testLabelA() {
		buildGraph1();
		maxMatch.setGraph(graph);
		
		graph.getVertexA(3).setLabel(new Label());
		
		List<Vertex> verticesB = new ArrayList<>();
		verticesB.add(graph.getVertexB(1));
		verticesB.add(graph.getVertexB(2));
		verticesB.add(graph.getVertexB(4));
		
		List<Vertex> verticesA = maxMatch.labelA(verticesB);
		
		assertEquals("Should label 3 vertices",3,verticesA.size());
		assertTrue("Should contain vertex 1 in B",verticesB.contains(new Vertex(1)));
		assertTrue("Should contain vertex 2 in B",verticesB.contains(new Vertex(2)));
		assertTrue("Should contain vertex 4 in B",verticesB.contains(new Vertex(4)));
		
	}
	
	@Test
	public void testLabelBWithBreakThrough() {
		buildGraph1();
		maxMatch.setGraph(graph);
		
		graph.getVertexA(3).setLabel(new Label());
		
		List<Vertex> verticesA = new ArrayList<>();
		verticesA.add(graph.getVertexA(1));
		verticesA.add(graph.getVertexA(2));
		verticesA.add(graph.getVertexA(4));
		
		graph.getVertexB(1).setLabel(new Label(graph.getVertexA(3)));
		graph.getVertexB(2).setLabel(new Label(graph.getVertexA(3)));
		graph.getVertexB(4).setLabel(new Label(graph.getVertexA(3)));
		
		
		List<Vertex> verticesB = maxMatch.labelB(verticesA);
		
		
		assertEquals("Status should be breakthrough", MaxMatch.Status.BREAKTHROUGH,maxMatch.getStatus());
		assertEquals("Break through vertex should be 3 in B", 3, maxMatch.getBreakthroughVertex().getId());
		
		
	}
	
	@Test
	public void testMaxMatchGraph2() {
		buildGraph2();
		maxMatch.setGraph(graph);
		
		Collection<Edge> matches = maxMatch.findMaxMatching();
		
		assertEquals(3, matches.size());
	}
	
	
	
	
	
	private void buildGraph1() {
		
		
		
		for (int i = 1; i <= 4; i++) {
			graph.addVertexA(new Vertex(i));
		}
		
		for (int i = 1; i <= 4; i++) {
			graph.addVertexB(new Vertex(i));
		}
		
		graph.addEdge(createEdge(0,1,1));
		
		graph.addEdge(createEdge(1,2,2));
		graph.addEdge(createEdge(2,2,3));
		
		graph.addEdge(createEdge(3,3,1));
		graph.addEdge(createEdge(4,3,2));
		graph.addEdge(createEdge(5,3,4));
		
		graph.addEdge(createEdge(6,4,2));
		graph.addEdge(createEdge(7,4,3));
		graph.addEdge(createEdge(8,4,4));
		
		graph.getEdge(0).getLink().setMatched(true);
		graph.getEdge(1).getLink().setMatched(true);
		graph.getEdge(8).getLink().setMatched(true);
		
		
		
	}
	
	private void buildGraph2() {
		for (int i = 1; i <= 4; i++) {
			graph.addVertexA(new Vertex(i));
		}
		
		for (int i = 1; i <= 3; i++) {
			graph.addVertexB(new Vertex(i));
		}
		
		graph.addEdge(createEdge(0,1,1));
		
		graph.addEdge(createEdge(1,2,1));
		graph.addEdge(createEdge(2,2,2));
		
		graph.addEdge(createEdge(3,3,2));
		graph.addEdge(createEdge(4,3,3));
		
		

		graph.addEdge(createEdge(5,4,3));

		
	}
		
		
	
	
	private Edge createEdge(int id, int vertexAId, int vertexBId) {
		Link link = new Link(id);
		Vertex vA = graph.getVertexA(vertexAId);
		Vertex vB = graph.getVertexB(vertexBId);
		return new Edge(vA,vB,link);

	}
	
	

}
