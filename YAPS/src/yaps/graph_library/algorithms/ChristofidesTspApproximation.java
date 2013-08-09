package yaps.graph_library.algorithms;

import java.io.IOException;
import java.util.List;

import yaps.graph_library.Edge;
import yaps.graph_library.Graph;
import yaps.graph_library.GraphReader;
import yaps.graph_library.Path;

public class ChristofidesTspApproximation extends GraphAlgorithm {
	private Path tspCycle;

	public ChristofidesTspApproximation(Graph g) {
		super(g);
	}
	
	public void calculateTspCycle() {
		AllShortestPaths shortestPaths = new AllShortestPaths(this.graph);
		shortestPaths.compute();
		
		Graph augmentedGraph = shortestPaths.toDistancesGraph(); 
		
		MinimumSpanningTree minTree = new MinimumSpanningTree(augmentedGraph);
		
		minTree.compute();
		
		Graph tree = minTree.getMinimumTree();
		
		int oddNodes = 0;
		for (int node = 0; node < tree.getNumNodes(); node++) {
			if (tree.getOutEdges(node).size() % 2 == 1) {
				oddNodes ++;
			}
		}

		System.out.println("Nodes with odd degree: " + oddNodes);
		
		//formar um subgrafo so com os nos ímpares
		Graph oddGraph = null;
		
		//emparelhar nos impares
		//OBS: nao é esse algoritmo
		//ver: http://en.wikipedia.org/wiki/Edmonds%27s_matching_algorithm
		MaximumBipartiteMatching matchingAlgorithm = new MaximumBipartiteMatching(oddGraph);
		
		matchingAlgorithm.compute();
		
		List<Edge> newEdges = matchingAlgorithm.getMatching();
		
		for (Edge edge : newEdges) {
			tree.addUndirectedEdge(edge.getSource(), edge.getTarget(), edge.getLength());
		}
		
		//formar o caminho euleriano...
		//Hierholzer's algorithm
		Path cycle = null;
		
		//criar os atalhos
		
		//trocar as arestas por caminhos do grafo original
		tspCycle = cycle.expandShortestPaths(shortestPaths);
	}

	public static void main(String[] args) throws IOException {
		Graph graph = GraphReader.readAdjacencyList("src\\tests\\graph\\grafo-11.txt");
		
		MinimumSpanningTree minTree = new MinimumSpanningTree(graph);
		
		minTree.compute();

		/* Custo da árvore esperado no "grafo-11": 39
		 */
		System.out.println();
		System.out.println("Cost: " + minTree.getMinimumTreeCost());
		System.out.println();
		System.out.print("The tree:");
		System.out.println(minTree.getMinimumTree());
		
		ChristofidesTspApproximation tspSolver = new ChristofidesTspApproximation(graph);
		
		tspSolver.calculateTspCycle();
	}
}
