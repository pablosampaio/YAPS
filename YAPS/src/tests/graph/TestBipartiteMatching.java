package tests.graph;

import java.io.IOException;

import yaps.graph_library.Graph;
import yaps.graph_library.GraphReader;
import yaps.graph_library.algorithms.HeuristicColoring;
import yaps.graph_library.algorithms.MaximumBipartiteMatching;

public class TestBipartiteMatching {


	public static void main(String[] args) throws IOException {
		Graph graph = GraphReader.readEdgeList("src\\tests\\graph\\grafo-23.txt"); /*/
		Graph graph = GraphReader.readAdjacencyList("src\\tests\\graph\\grafo-24.txt"); //*/

		testBipartiteness(graph);
		
		MaximumBipartiteMatching matching = new MaximumBipartiteMatching(graph);
		
		matching.compute(); 
		//matching.computeAlternative();
			
		System.out.println("Tamanho do Emparelhamento M�ximo: " + matching.getMatchingSize());
		System.out.println("Emparelhamento M�ximo: " + matching.getMatching());		
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
		
		System.out.print("\nParti��o B : ");
		for (int v = 0; v < graph.getNumNodes(); v++) {
			if (coloring.getColor(v) == 2) {
				System.out.print(v + " ");
			}
		}
		
		System.out.println();
	}

}