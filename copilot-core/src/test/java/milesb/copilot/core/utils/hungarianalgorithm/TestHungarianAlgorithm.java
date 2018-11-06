package milesb.copilot.core.utils.hungarianalgorithm;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import milesb.copilot.core.utils.hungarianalgorithm.BipartiteGraph;
import milesb.copilot.core.utils.hungarianalgorithm.Edge;
import milesb.copilot.core.utils.hungarianalgorithm.HungarianAlgorithm;
import milesb.copilot.core.utils.hungarianalgorithm.Label;
import milesb.copilot.core.utils.hungarianalgorithm.Link;
import milesb.copilot.core.utils.hungarianalgorithm.Vertex;


public class TestHungarianAlgorithm {

	private HungarianAlgorithm hungarianAlgorithm;
	

	

	@Before
	public void setUp() throws Exception {

		hungarianAlgorithm = new HungarianAlgorithm();
		
		

	}

	
	
	@Test
	public void testReviseCostMatrixRows() {
		double[][] scores = new double[][] {{8.0,4.0,6.0,7.0},
            {10.0,12.0,8.0,14.0},
            {9.0,6.0,11.0,15.0},
            {12.0,8.0,14.0,8.0}};
		
		hungarianAlgorithm.setScores(scores);
		hungarianAlgorithm.reviseCostMatrixRows();
		double[] expectedRowWeights = new double[] {4.0,8.0,6.0,8.0};
		double[][] expectedRevisedCosts = new double[][] {{4.0,0.0,2.0,3.0},
			                                              {2.0,4.0,0.0,6.0},
			                                              {3.0,0.0,5.0,9.0},
			                                              {4.0,0.0,6.0,0.0}};
		
		
			
		
		
		assertArrayEquals(expectedRowWeights,hungarianAlgorithm.getRowWeights(),0.01);
		
		assertArrayEquals(expectedRevisedCosts[0],hungarianAlgorithm.getWorkingScores()[0],0.01);
		assertArrayEquals(expectedRevisedCosts[1],hungarianAlgorithm.getWorkingScores()[1],0.01);
		assertArrayEquals(expectedRevisedCosts[2],hungarianAlgorithm.getWorkingScores()[2],0.01);
		assertArrayEquals(expectedRevisedCosts[3],hungarianAlgorithm.getWorkingScores()[3],0.01);
		
	}
	
	@Test
	public void testReviseCostMatrixCols() {
		double[][] inputScores = new double[][] {{4.0,0.0,2.0,3.0},
                                 {2.0,4.0,0.0,6.0},
                                 {3.0,0.0,5.0,9.0},
                                 {4.0,0.0,6.0,0.0}};
		
		
		double[] expectedColWeights = new double[] {2.0,0.0,0.0,0.0};
		double[][] expectedRevisedCosts = new double[][] {{2.0,0.0,2.0,3.0},
			                                              {0.0,4.0,0.0,6.0},
			                                              {1.0,0.0,5.0,9.0},
			                                              {2.0,0.0,6.0,0.0}};
		
		
			
		hungarianAlgorithm.setScores(inputScores);
		hungarianAlgorithm.reviseCostMatrixColumns();
		
		assertArrayEquals(expectedColWeights,hungarianAlgorithm.getColWeights(),0.01);
		
		assertArrayEquals(expectedRevisedCosts[0],hungarianAlgorithm.getWorkingScores()[0],0.01);
		assertArrayEquals(expectedRevisedCosts[1],hungarianAlgorithm.getWorkingScores()[1],0.01);
		assertArrayEquals(expectedRevisedCosts[2],hungarianAlgorithm.getWorkingScores()[2],0.01);
		assertArrayEquals(expectedRevisedCosts[3],hungarianAlgorithm.getWorkingScores()[3],0.01);
		
	}
	
	@Test
	public void testConstructBipartiteGraph() {
		double[][] inputCosts = new double[][] {{2.0,0.0,2.0,3.0},
            {0.0,4.0,0.0,6.0},
            {1.0,0.0,5.0,9.0},
            {2.0,0.0,6.0,0.0}};
            
        hungarianAlgorithm.setScores(inputCosts);
        BipartiteGraph graph = hungarianAlgorithm.constructPartialGraph();
         
        
        assertEquals(4, graph.getVerticesA().size());
        assertEquals(4, graph.getVerticesB().size());
        assertEquals(6, graph.getEdges().size());
        assertEquals(1, graph.getVertexA(0).getConnections().size());
      
	}
	
	@Test
	public void testModifyPartialGraph() {
		BipartiteGraph graph = new BipartiteGraph();
		for (int i = 0; i < 4; i++) {
			graph.addVertexA(new Vertex(i));
			graph.addVertexB(new Vertex(i));
		}
		
		graph.addEdge(createEdge(0,0,1,graph));
		graph.addEdge(createEdge(1,1,0,graph));
		graph.addEdge(createEdge(2,1,2,graph));
		graph.addEdge(createEdge(3,2,1,graph));
		graph.addEdge(createEdge(4,3,1,graph));
		graph.addEdge(createEdge(5,3,3,graph));
		
		Vertex va1 = graph.getVertexA(0);
		Vertex va3 = graph.getVertexA(2);
		Vertex vb2 = graph.getVertexB(1);
		va1.setLabel(new Label(vb2));
		va3.setLabel(new Label());
		vb2.setLabel(new Label(va3));
		
		hungarianAlgorithm.setScores(new double[][] {{2.0,0.0,2.0,3.0},
                                                     {0.0,4.0,0.0,6.0},
                                                     {1.0,0.0,5.0,9.0},
                                                     {2.0,0.0,6.0,0.0}});
		hungarianAlgorithm.setRowWeights(new double[] {4.0,8.0,6.0,8.0});
		hungarianAlgorithm.setColWeights(new double[] {2.0,0.0,0.0,0.0});
			
		
		hungarianAlgorithm.modifyPartialGraph(graph);
		
		double[] expectedRowWeights = {5.0,8.0,7.0,8.0};
		double[] expectedColHeights = {2.0,-1.0,0.0,0.0};
		
		double[][] expectedScores = {{1.0,0.0,1.0,2.0},
				                    {0.0,5.0,0.0,6.0},
				                    {0.0,0.0,4.0,8.0},
				                    {2.0,1.0,6.0,0.0}};
		
		
		
		
		assertArrayEquals("Incorrect row weights",expectedRowWeights, hungarianAlgorithm.getRowWeights(), 0.01);
		assertArrayEquals("Incorrect col weights",expectedColHeights, hungarianAlgorithm.getColWeights(),0.01);
		
		double[][] actualScores = hungarianAlgorithm.getWorkingScores();
		for (int row = 0; row < actualScores.length; row++) {
			assertArrayEquals("Incorrect scores at row "+row, expectedScores[row], actualScores[row], 0.01);
		}
		
		
		
	}
	
	@Test
	public void testCalcScore() {
		double[][] scores = {{1.0,0.0,1.0,2.0},
                             {0.0,5.0,0.0,6.0},
                             {0.0,0.0,4.0,8.0},
                             {2.0,1.0,6.0,0.0}};
		
		double[] rowWeights = {10.0,8.0,5.0,6.0};
		double[] colWeights = {-4.0,0.0,0.0,3.0};
		
		hungarianAlgorithm.setScores(scores);
		hungarianAlgorithm.setRowWeights(rowWeights);
		hungarianAlgorithm.setColWeights(colWeights);
		
		assertEquals(28.0,hungarianAlgorithm.calcScore(), 0.01);
	}
	
	
	@Test
	public void testMatch1() {
		double[][] scores = new double[][] {{8.0,4.0,6.0,7.0},
            {10.0,12.0,8.0,14.0},
            {9.0,6.0,11.0,15.0},
            {12.0,8.0,14.0,8.0}};
		
        
        Collection<Edge> matchings = hungarianAlgorithm.match(cloneArray(scores));
		
		assertEquals("Incorrect number of matchings",4, matchings.size());
		
		double score =calcScoreFromMatchings(matchings, scores);
		
		assertEquals("Total edge scores is incorrect", 29, score, 0.01);
		
			
		
	}
	
	
	
	
	
	@Test
	public void testMatch2() {
		double[][] scores = new double[][] {{6.0,12.0,15.0,15.0},
                                            {4.0,8.0,9.0,11.0},
                                            {10.0,5.0,7.0,8.0},
                                            {12.0,10.0,6.0,9.0}};
		
        

                                            
        Collection<Edge> matchings = hungarianAlgorithm.match(cloneArray(scores));
		
		assertEquals("Incorrect number of matchings",4, matchings.size());
		
		double score =calcScoreFromMatchings(matchings, scores);
		
		assertEquals("Total edge scores is incorrect", 28, score, 0.01);
			
		
	}
	
	private double[][] cloneArray(double[][] array) {
		double[][] cloned = new double[array.length][array[0].length];
		for (int row = 0; row < array.length; row++) {
			System.arraycopy(array[row], 0, cloned[row], 0, array[0].length);
		}
		
		return cloned;
	}
	
	
	@Test
	public void testWhenMoreAThanB() {
		double[][] scores = new double[][] {{5.0}, {3.0}};
		
		 Collection<Edge> matchings = hungarianAlgorithm.match(scores);
		 
		 assertEquals(1,matchings.size());
		 
		 double score =calcScoreFromMatchings(matchings, scores);
		 
		 assertEquals(3.0, score, 0.01);
			
	}
	
	@Test
	public void testWhenMoreBThanA() {
		double[][] scores = new double[][] {{5.0,3.0}};
		
        Collection<Edge> matchings = hungarianAlgorithm.match(scores);
		 
		assertEquals(1,matchings.size());
		 
		double score =calcScoreFromMatchings(matchings, scores);
		 
		assertEquals(3.0, score, 0.01);
	}
	
	@Test
	public void testDummyExample() {
		double[][] scores = {{100.0,3.0,2.0,100.0,100.0},
				              {4.0,5.0,100.0,100.0,2.0},
				              {100.0,100.0,4.0,6.0,3.0},
				              {100.0,1.0,100.0,5.0,100.0}};
		
		Collection<Edge> matchings = hungarianAlgorithm.match(scores);
		
		assertEquals(4,matchings.size());
		
		assertEquals(10,calcScoreFromMatchings(matchings, scores),0.01);
		
		
	}
	
	private double calcScoreFromMatchings(Collection<Edge> matchings, double[][] scores) {
		double score = 0.0;
		for (Edge edge: matchings) {
			Vertex va = edge.getVertexA();
			Vertex vb = edge.getVertexB();
			score += scores[va.getId()][vb.getId()];
		}
		
		return score;
	}
	
	
	private Edge createEdge(int id, int a, int b, BipartiteGraph graph) {
	   Link link = new Link(id);
	   Vertex vertexA = graph.getVertexA(a);
	   Vertex vertexB = graph.getVertexB(b);

	   return new Edge(vertexA, vertexB, link);

	}
		
	
	

}
