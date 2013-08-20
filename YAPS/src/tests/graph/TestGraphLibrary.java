package tests.graph;

import java.io.IOException;

import yaps.graph_library.Graph;
import yaps.graph_library.GraphFileFormat;
import yaps.graph_library.GraphFileUtil;
import yaps.graph_library.Path;
import yaps.graph_library.algorithms.AllShortestPaths;


public class TestGraphLibrary {

	public static void main(String[] args) throws IOException {
		//testEditing(); 
		//testReadingAndShortestPaths();
		testFileFormatConversion(); 
	}

	private static void testFileFormatConversion() throws IOException {
		String[] maps = { "map_cicles_corridor", "map_city_traffic", "map_grid",
				"map_islands", "map_random_directed_1", "map_random_directed_2" };
		
		for (String mapName : maps) {
			GraphFileUtil.convert("maps/" + mapName + ".xml", GraphFileFormat.SIMPATROL, 
					"tmp/" + mapName + ".adj", GraphFileFormat.ADJACENCY_LIST);
			System.out.println("Converted " + mapName);
		}
		
		System.out.println("Ok");
	}

	private static void testReadingAndShortestPaths() throws IOException {
		Graph graph = GraphFileUtil.readAdjacencyList("src/tests/graph/island11");
		
		//imprime o grafo na forma de matriz e de listas de adjacencias
		System.out.println(graph);

		//calcula os menores caminhos
		AllShortestPaths minPaths = new AllShortestPaths(graph);
		minPaths.compute();
		
		int origin = 2;  //you may choose any other node
		
		for (int destiny = 0; destiny < graph.getNumNodes(); destiny++) {
			Path path = minPaths.getPath(origin, destiny);
			System.out.printf("Menor caminho de n%d para n%d: %s, custo: %s\n", 
								origin, destiny, path, path.getCost(graph));
		}
	}

	private static void testEditing() {
		Graph graph = new Graph(5);
		
		graph.addArc(0, 1, 10);
		graph.addArc(0, 4, 50);
		graph.addArc(1, 4, 10);
		graph.addArc(1, 2, 10);
		graph.addArc(1, 3, 50);
		graph.addUndirectedEdge(2, 3, 12);
		
		System.out.println(graph);
		
		graph.removeEdge(3, 2); //removes both directions
		
		System.out.println(graph);
	}
}
