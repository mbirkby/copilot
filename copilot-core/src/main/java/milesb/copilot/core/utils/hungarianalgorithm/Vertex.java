package milesb.copilot.core.utils.hungarianalgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


public class Vertex {
    private int id;
    private Collection<Connection> connections;
    private Label label;
    
	public Vertex(int id) {
    	this.id = id;
    	connections = new ArrayList<>();
    }
    
    public int getId() {
		return id;
	}
    
    void addConnection(Connection connection) {
    	connections.add(connection);
    }
    
    Collection<Connection> getConnections() {
    	return Collections.unmodifiableCollection(connections);
    }
    
    Connection getConnectionTo(Vertex vertex) {
    	Connection foundConnection = null;
    	Iterator<Connection> i = connections.iterator();
    	while (i.hasNext() && foundConnection == null) {
    		Connection currentConnection = i.next();
    		Vertex currentVertex = currentConnection.getVertex();
    		if (currentVertex.equals(vertex)) {
    			foundConnection = currentConnection;
    		}
    	}
    	
    	return foundConnection;
    }
    
    boolean isConnectedTo(Vertex vertex) {
    	Connection connection = getConnectionTo(vertex);
    	
    	return connection != null;
    }
    
    public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}
	
	boolean hasLabel() {
		return label != null;
	}

	
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;

		return id == other.id;
	}
    
    
    
}
