package tests.graph;

import java.io.IOException;

import yaps.graph_library.Graph;
import yaps.graph_library.GraphReader;
import yaps.graph_library.Path;
import yaps.graph_library.algorithms.AllPairsShortestPaths;


public class TestGraphLibrary {

	public static void main(String[] args) throws IOException {
		//testEditing();
		testReadingAndShortestPaths();
	}

	private static void testReadingAndShortestPaths() throws IOException {
		Graph graph = GraphReader.readAdjacencyList("src/tests/graph/island11");
		
		//imprime o grafo na forma de matriz e de listas de adjacencias
		System.out.println(graph);

		//calcula os menores caminhos
		AllPairsShortestPaths minPaths = new AllPairsShortestPaths();
		minPaths.computeShortestPaths(graph);
		
		int origin = 2;  //you may choose any other node
		
		for (int destiny = 0; destiny < graph.getNumNodes(); destiny++) {
			Path path = minPaths.getPath(origin, destiny);
			System.out.printf("Menor caminho de n%d para n%d: %s, custo: %s\n", 
								origin, destiny, path, path.getCost(graph));
		}
	}

	public static void testEditing() {
		Graph graph = new Graph(5);
		
		graph.addEdge(0, 1, 1.0);
		graph.addEdge(0, 4, 5.0);
		graph.addEdge(1, 4, 1.0);
		graph.addEdge(1, 2, 1.0);
		graph.addEdge(1, 3, 5.0);
		graph.addUndirectedEdge(2, 3, 1.2);
		
		System.out.println(graph);
		
		graph.removeEdge(3, 2);
		
		System.out.println(graph);
	}
}
