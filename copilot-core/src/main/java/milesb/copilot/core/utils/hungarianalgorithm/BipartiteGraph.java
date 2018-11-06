package milesb.copilot.core.utils.hungarianalgorithm;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import java.util.Map;

class BipartiteGraph {
    private Map<Integer,Vertex> verticesA;
    private Map<Integer, Vertex> verticesB;
    private Map<Integer, Edge> edges;
    
  
    
    
    BipartiteGraph() {
    	verticesA = new HashMap<>();
    	verticesB = new HashMap<>();
    	edges = new HashMap<>();
    }
    
    
    void addVertexA(Vertex vertex) {
    	verticesA.put(vertex.getId(), vertex);
    }
    
    void addVertexB(Vertex vertex) {
    	verticesB.put(vertex.getId(), vertex);
    }
    
    int getSizeA() {
    	return verticesA.size();
    }
    
    int getSizeB() {
    	return verticesB.size();
    }
    
    Vertex getVertexA(int id) {
       return verticesA.get(id);	
    }
    
    Vertex getVertexB(int id) {
    	return verticesB.get(id);
    }
    
    void addEdge(Edge edge) {
        Vertex vertexA = edge.getVertexA();
        Vertex vertexB = edge.getVertexB();
        Link link = edge.getLink();
        
        vertexA.addConnection(new Connection(link,vertexB));
        vertexB.addConnection(new Connection(link, vertexA));
        
        edges.put(edge.getId(), edge);
    }
    
    
    Collection<Vertex> getVerticesA() {
    	return  Collections.unmodifiableCollection(verticesA.values());
    	
    }
    
    Collection<Vertex> getVerticesB() {
    	return Collections.unmodifiableCollection(verticesB.values());    	
    }
    
    Collection<Edge> getEdges() {
    	return Collections.unmodifiableCollection(edges.values());
    }
    
    Edge getEdge(int id) {
    	return edges.get(id);
    }
    
    
    
    
    
}
