package milesb.copilot.core.utils.hungarianalgorithm;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestVertex {

	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testAddConnection() {
		Vertex thisVertex = new Vertex(0);
		
		
		thisVertex.addConnection(new Connection(new Link(0), new Vertex(0)));
		thisVertex.addConnection(new Connection(new Link(1), new Vertex(1)));
		thisVertex.addConnection(new Connection(new Link(2), new Vertex(2)));
		
		assertEquals("Incorrect number of connections",3, thisVertex.getConnections().size());
	}
	
	@Test
	public void testGetConnectionsTo() {
		Vertex thisVertex = new Vertex(0);
		
		Vertex v1 = new Vertex(1);
		Vertex v2 = new Vertex(2);
		Vertex v3 = new Vertex(3);
		Vertex v4 = new Vertex(4);
		
		Connection c1 = new Connection(new Link(0), v1);
		Connection c2 = new Connection(new Link(1), v2);
		Connection c3 = new Connection(new Link(2), v3);
		
				
		thisVertex.addConnection(c1);
		thisVertex.addConnection(c2);
		thisVertex.addConnection(c3);
		
		assertEquals("Incorrcet connection to v1",c1, thisVertex.getConnectionTo(v1));
		assertEquals("Incorrect connection to v2",c2, thisVertex.getConnectionTo(v2));
		assertEquals("Incorrect connectoin to v3",c3, thisVertex.getConnectionTo(v3));
		assertNull("Should be no connection to v4",thisVertex.getConnectionTo(v4));
	}
	
	@Test
	public void testIsConnectedTo() {
		Vertex thisVertex = new Vertex(0);
		
		Vertex v1 = new Vertex(1);
		Vertex v2 = new Vertex(2);
		Vertex v3 = new Vertex(3);
		Vertex v4 = new Vertex(4);
		
		Connection c1 = new Connection(new Link(0), v1);
		Connection c2 = new Connection(new Link(1), v2);
		Connection c3 = new Connection(new Link(2), v3);
		
				
		thisVertex.addConnection(c1);
		thisVertex.addConnection(c2);
		thisVertex.addConnection(c3);
		
		assertTrue("Incorrcet connection to v1",thisVertex.isConnectedTo(v1));
		assertTrue("Incorrect connection to v2", thisVertex.isConnectedTo(v2));
		assertTrue("Incorrect connectoin to v3", thisVertex.isConnectedTo(v3));
		assertFalse("Should be no connection to v4",thisVertex.isConnectedTo(v4));
	}

}
