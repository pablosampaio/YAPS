package tests;

import yaps.graph_library.Graph;
import yaps.graph_library.algorithms.AllPairsShortestPaths;

public class TestGraphLibrary {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Graph graph = new Graph(5);
		
		graph.addEdge(0, 1, 1.0);
		graph.addEdge(0, 4, 5.0);
		graph.addEdge(1, 4, 1.0);
		graph.addEdge(1, 2, 1.0);
		graph.addEdge(1, 3, 5.0);
		graph.addEdge(2, 3, 1.0);
		
		//graph = graph.toTranspose();
		
		AllPairsShortestPaths paths = new AllPairsShortestPaths();
		
		paths.computeShortestPaths(graph);
	}

}
