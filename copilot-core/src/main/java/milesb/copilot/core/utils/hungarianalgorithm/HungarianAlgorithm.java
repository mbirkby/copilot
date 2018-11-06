package milesb.copilot.core.utils.hungarianalgorithm;


import java.util.Collection;
import java.util.Iterator;


public class HungarianAlgorithm {

	private double[] rowWeights;
	private double[] colWeights;
	private MaxMatch maxMatch;

	private double[][] scores;
	private double[][] workingScores;
	


	HungarianAlgorithm() {

	}

	double[][] getWorkingScores() {
		return workingScores;
	}

	void setScores(double[][] scores) {
		
		this.scores = scores;
		int rows = scores.length;
		int cols = scores[0].length;
		
		if (rows > cols) {
			this.workingScores = createWorkingArray(rows, scores);		}
		else if (cols > rows) {
			this.workingScores = createWorkingArray(cols, scores);
		}
		else {
		  this.workingScores = createWorkingArray(rows, scores);
		}
		
		rowWeights = new double[workingScores.length];
		colWeights = new double[workingScores[0].length];
	}

	double[] getRowWeights() {
		return rowWeights;
	}
	
	private double[][] createWorkingArray(int size, double[][] scores) {
		
		
		double[][] newArray = new double[size][size];
		//copy existing scores
		for (int row = 0; row < scores.length; row++) {
            System.arraycopy(scores[row], 0, newArray[row], 0, scores[0].length);
		}
		
		//set dummy rows to high score
		for (int row = scores.length; row < newArray.length; row++) {
			for (int col = 0; col < newArray[0].length; col++) {
			  newArray[row][col] = 100.0;
			}
		}
		
		//set dummy columns to high score
		for (int col = scores[0].length;col < newArray[0].length; col++) {
			for (int row = 0; row < newArray.length; row++) {
				newArray[row][col] = 100.0;
			}
		}
		
		return newArray;
	}

	void setRowWeights(double[] rowWeights) {
		this.rowWeights = rowWeights;
	}

	double[] getColWeights() {
		return colWeights;
	}

	void setColWeights(double[] colWeights) {
		this.colWeights = colWeights;
	}

	public Collection<Edge> match(double[][] scores) {
        
		setScores(scores);
		maxMatch = new MaxMatch();
		
        Collection<Edge> matchings = null;
		reviseCostMatrix();
		boolean cont = true;
		while (cont) {
			
			BipartiteGraph graph = constructPartialGraph();
			maxMatch.setGraph(graph);
			matchings = maxMatch.findMaxMatching();

			if (maxMatch.getStatus() != (MaxMatch.Status.MAX_MATCHING)) {
				modifyPartialGraph(graph);
			} else {
				cont = false;
			}
		}
		
        removeDummyMatchings(matchings);

		return matchings;

	}
	
	
	private void removeDummyMatchings(Collection<Edge> matchings) {
		Iterator<Edge> iterator = matchings.iterator();
		
		while (iterator.hasNext()) {
			Edge edge = iterator.next();
			
			Vertex va = edge.getVertexA();
			Vertex vb = edge.getVertexB();
			
			if (va.getId() >= scores.length || vb.getId() >= scores[0].length) {
				iterator.remove();
			}
			
			
		}
	}
	

	private void reviseCostMatrix() {
		reviseCostMatrixRows();
		reviseCostMatrixColumns();
	}

	void reviseCostMatrixRows() {
		for (int row = 0; row < workingScores.length; row++) {
			double smallest = workingScores[row][0];
			for (int col = 1; col < workingScores[0].length; col++) {
				if (workingScores[row][col] < smallest) {
					smallest = workingScores[row][col];
				}
			}

			rowWeights[row] = smallest;

			for (int col = 0; col < workingScores[0].length; col++) {
				workingScores[row][col] = workingScores[row][col] - smallest;
			}
		}

	}

	void reviseCostMatrixColumns() {
		for (int col = 0; col < workingScores[0].length; col++) {
			double smallest = workingScores[0][col];
			for (int row = 1; row < workingScores.length; row++) {
				if (workingScores[row][col] < smallest) {
					smallest = workingScores[row][col];
				}
			}

			colWeights[col] = smallest;

			for (int row = 0; row < workingScores.length; row++) {
				workingScores[row][col] = workingScores[row][col] - smallest;
			}
		}
	}

	BipartiteGraph constructPartialGraph() {
		BipartiteGraph graph = new BipartiteGraph();

		for (int row = 0; row < workingScores.length; row++) {
			graph.addVertexA(new Vertex(row));
		}

		for (int col = 0; col < workingScores[0].length; col++) {
			graph.addVertexB(new Vertex(col));
		}

		int linkId = 0;
		for (int row = 0; row < workingScores.length; row++) {
			for (int col = 0; col < workingScores[0].length; col++) {
				if (workingScores[row][col] == 0.0) {
					Link link = new Link(linkId++);
					Edge edge = new Edge(graph.getVertexA(row), graph.getVertexB(col), link);
					graph.addEdge(edge);
				}
			}
		}

		return graph;

	}

	void modifyPartialGraph(BipartiteGraph graph) {
		double delta = getDelta(graph);
		increaseRowWeightsByDelta(delta, graph);
		decreaseColWeightsByDelta(delta, graph);
		decreaseScoreRowsByDelta(delta, graph);
		increaseScoreColsByDelta(delta, graph);

	}

	private void increaseRowWeightsByDelta(double delta, BipartiteGraph graph) {
		for (int i = 0; i < rowWeights.length; i++) {
			Vertex va = graph.getVertexA(i);
			if (va.hasLabel()) {
				rowWeights[i] = rowWeights[i] + delta;
			}
		}
	}

	private void decreaseColWeightsByDelta(double delta, BipartiteGraph graph) {
		for (int i = 0; i < colWeights.length; i++) {
			Vertex vb = graph.getVertexB(i);
			if (vb.hasLabel()) {
				colWeights[i] = colWeights[i] - delta;
			}
		}
	}

	private void decreaseScoreRowsByDelta(double delta, BipartiteGraph graph) {
		for (int row = 0; row < workingScores.length; row++) {
			Vertex va = graph.getVertexA(row);
			if (va.hasLabel()) {
				for (int col = 0; col < workingScores[0].length; col++) {
					workingScores[row][col] = workingScores[row][col] - delta;
				}
			}
		}
	}

	private void increaseScoreColsByDelta(double delta, BipartiteGraph graph) {
		for (int col = 0; col < workingScores[0].length; col++) {
			Vertex vb = graph.getVertexB(col);
			if (vb.hasLabel()) {
				for (int row = 0; row < workingScores.length; row++) {					
					workingScores[row][col] = workingScores[row][col] + delta;					
				}
			}
		}
	}

	private double getDelta(BipartiteGraph graph) {
		double delta = Double.MAX_VALUE;
		for (int row = 0; row < workingScores.length; row++) {
			Vertex va = graph.getVertexA(row);
			if (va.hasLabel()) {
				for (int col = 0; col < workingScores[0].length; col++) {
					Vertex vb = graph.getVertexB(col);
					if (!vb.hasLabel()) {
						if (workingScores[row][col] < delta) {
							delta = workingScores[row][col];
						}
					}
				}
			}
		}

		return delta;
	}
	
	double calcScore() {
		double score = 0.0;

        for (double rowWeight : rowWeights) {
            score += rowWeight;
        }

        for (double colWeight : colWeights) {
            score += colWeight;
        }
		
		return score;
		
	}

}
