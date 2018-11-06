package milesb.copilot.core.utils.hungarianalgorithm;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestBipartiteGraph {

	private BipartiteGraph graph;

	@Before
	public void setup() {
		graph = new BipartiteGraph();
	}

	@Test
	public void testAddVertexAIncrementsSize() {
		graph.addVertexA(new Vertex(0));
		graph.addVertexA(new Vertex(1));

		assertEquals("Incorrect number of vertices", 2, graph.getSizeA());

	}

	@Test
	public void testAddVertexBIncrementsSize() {
		graph.addVertexB(new Vertex(0));
		graph.addVertexB(new Vertex(1));

		assertEquals("Incorrect number of vertices", 2, graph.getSizeB());
	}
	
	@Test
	public void testGetVertexA() {
		Vertex[] vertices = {new Vertex(1), new Vertex(2), new Vertex(3)};
		
		graph.addVertexA(vertices[0]);
		graph.addVertexA(vertices[1]);
		graph.addVertexA(vertices[2]);
		
		assertEquals("Vertex 0 not returned correctly", vertices[0], graph.getVertexA(1));
		assertEquals("Vertex 1 not returned correctly", vertices[1], graph.getVertexA(2));
		assertEquals("Vertex 2 not returned correctly", vertices[2], graph.getVertexA(3));
		
	}
	
	@Test
	public void testGetVertexB() {
		Vertex[] vertices = {new Vertex(0), new Vertex(1), new Vertex(2)};
		
		graph.addVertexB(vertices[0]);
		graph.addVertexB(vertices[1]);
		graph.addVertexB(vertices[2]);
		
		assertEquals("Vertex 0 not returned correctly", vertices[0], graph.getVertexB(0));
		assertEquals("Vertex 1 not returned correctly", vertices[1], graph.getVertexB(1));
		assertEquals("Vertex 2 not returned correctly", vertices[2], graph.getVertexB(2));
		
	}
	
	@Test
	public void testGetVerticesA() {
		graph.addVertexA(new Vertex(0));
		graph.addVertexA(new Vertex(1));

		Collection<Vertex> vertices = graph.getVerticesA();
		assertEquals("Incorrect number of vertices", 2, vertices.size());

	}

	@Test
	public void testGetVerticesB() {
		graph.addVertexB(new Vertex(0));
		graph.addVertexB(new Vertex(1));

		Collection<Vertex> vertices = graph.getVerticesB();
		
		assertEquals("Incorrect number of vertices", 2, vertices.size());
		
	}
	
	@Test
	public void testAddOneEdgeReturnsOneEdge() {
       Vertex a1 = new Vertex(1);
       Vertex b1 = new Vertex(1);
		
		graph.addEdge(createEdge(0,a1,b1));
		
		assertEquals(1,graph.getEdges().size());
		
	}
	
	@Test
	public void testAddEdgeCreatesConnections() {
		Vertex a1 = new Vertex(1);
		Vertex b1 = new Vertex(1);
		
		graph.addVertexA(a1);
		graph.addVertexB(b1);
		
		graph.addEdge(createEdge(0,a1,b1));
		

		assertEquals("VertexA not connected to 1 vertex", 1, a1.getConnections().size() );		
		assertEquals("VertexB not connected to 1 vertex", 1, b1.getConnections().size() );
		
	}
	
	
	
	
	@Test
	public void testAddEdgeCreatesCorrectConnections() {
		Vertex a1 = new Vertex(1);
		Vertex b1 = new Vertex(1);
		
		graph.addVertexA(a1);
		graph.addVertexB(b1);
		
		graph.addEdge(createEdge(0,a1,b1)); 
        
		assertTrue("Vertex a1 not connected to vertex b1", a1.isConnectedTo(b1));	
		assertTrue("Vertex b1 not connected to vertex a2", b1.isConnectedTo(a1));
		
		
	}
	
	private Edge createEdge(int id, Vertex a1, Vertex b1) {
		Link link = new Link(id);
		return new Edge(a1,b1,link);
	}

}
