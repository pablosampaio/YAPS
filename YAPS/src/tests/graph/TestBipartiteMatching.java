package tests.graph;

import java.io.IOException;

import yaps.graph_library.Graph;
import yaps.graph_library.GraphReader;
import yaps.graph_library.algorithms.BipartiteMatching;
import yaps.graph_library.algorithms.HeuristicColoring;

public class TestBipartiteMatching {


	public static void main(String[] args) throws IOException {
		Graph graph = GraphReader.readEdgeList("src\\tests\\graph\\grafo-23.txt"); /*/
		Graph graph = GraphReader.readAdjacencyList("src\\tests\\graph\\grafo-24.txt"); //*/

		testBipartiteness(graph);
		
		BipartiteMatching matching = new BipartiteMatching();
		
		matching.findMaxMatching(graph); 
			
		System.out.println("Tamanho do Emparelhamento Máximo: " + matching.getMaxMatchingSize());
		System.out.println("Emparelhamento Máximo: " + matching.getMaxMatching());		
	}
	
	
	public static void testBipartiteness(Graph graph) {
		HeuristicColoring coloring = new HeuristicColoring();
		
		coloring.bfsColoring(graph);
		
		System.out.print("Particao A : ");
		for (int v = 0; v < graph.getNumNodes(); v++) {
			if (coloring.getColor(v) == 1) {
				System.out.print(v + " ");
			}
		}
		
		System.out.print("\nPartição B : ");
		for (int v = 0; v < graph.getNumNodes(); v++) {
			if (coloring.getColor(v) == 2) {
				System.out.print(v + " ");
			}
		}
		
		System.out.println();
	}

}
