package tests.graph;

import java.io.IOException;

import yaps.graph_library.Graph;
import yaps.graph_library.GraphReader;
import yaps.graph_library.algorithms.MinimumSpanningTree;


public class TestMinSpanningTree {
	
	public static void main(String[] args) throws IOException {
		Graph graph = GraphReader.readAdjacencyList("src\\tests\\graph\\grafo-11.txt");
		
		MinimumSpanningTree minTree = new MinimumSpanningTree(graph);
		
		minTree.compute();

		/* Custo da �rvore esperado no "grafo-11": 39
		 */
		System.out.println();
		System.out.println("Cost: " + minTree.getMinimumTreeCost());
		System.out.println();
		System.out.print("The tree:");
		System.out.println(minTree.getMinimumTree());
	}
	
}
