package tests.graph;

import java.io.IOException;

import yaps.graph_library.Graph;
import yaps.graph_library.GraphReader;
import yaps.graph_library.algorithms.HeuristicColoring;


public class TestProperColoring {

	public static void main(String[] args) throws IOException {
		Graph graph = GraphReader.readAdjacencyList("src\\tests\\graph\\grafo-11.txt");
		HeuristicColoring colorer = new HeuristicColoring();
		
		long time;
		int colors;
		
		System.out.printf("Graph with %s nodes \n\n", graph.getNumNodes());
		
		/* Resultados esperados no "grafo-11": bfs 4 / lcf 4 / lcfx 4
		 */
		
		time = System.currentTimeMillis();
		colors = colorer.bfsColoring(graph);
		time = System.currentTimeMillis() - time;
		System.out.printf(">> bfs: %s (%sms)\n", colors, time);
		
		printColoring(graph, colorer);

		time = System.currentTimeMillis();
		colors = colorer.leastConstrainedFirstColoring(graph);
		time = System.currentTimeMillis() - time;
		System.out.printf(">> lcf: %s (%sms)\n", colors, time);
		
		printColoring(graph, colorer);
				
		time = System.currentTimeMillis();
		colors = colorer.leastConstrainedFirstColoringX(graph);
		time = System.currentTimeMillis() - time;
		System.out.printf(">> lcfx: %s (%sms)\n", colors, time);
		
		printColoring(graph, colorer);
	}
	
	private static void printColoring(Graph graph, HeuristicColoring colorer) {
		System.out.print("   coloring:\n   ");
		
		for (int i = 0; i < graph.getNumNodes(); i++) {
			System.out.printf("node%d/%d ", i, colorer.getColor(i));
		}
		System.out.println();
		System.out.println();
	}
}
