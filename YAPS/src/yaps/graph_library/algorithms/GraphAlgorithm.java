package yaps.graph_library.algorithms;

import yaps.graph_library.Graph;


public class GraphAlgorithm {
	protected Graph graph;
	
	protected static final int INFINITE = Integer.MAX_VALUE / 2; //to avoid overflow in sums


	public GraphAlgorithm(Graph g) {
		this.graph = g;
	}
	
	public Graph getGraph() {
		return this.graph;
	}
	
}
