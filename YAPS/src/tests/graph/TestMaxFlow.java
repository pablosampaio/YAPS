package tests.graph;

import java.io.IOException;

import yaps.graph_library.Graph;
import yaps.graph_library.GraphFileUtil;
import yaps.graph_library.algorithms.MaximumFlow;


public class TestMaxFlow {
	
	public static void main(String[] args) throws IOException {
		Graph graph = GraphFileUtil.readAdjacencyList("src\\tests\\graph\\grafo-11.txt");

		MaximumFlow maxFlow = new MaximumFlow(graph);
		
		maxFlow.compute(0, 6);

		/* Fluxo maximo esperado no "grafo-11": 12
		 */
		System.out.println("Fluxo Máximo:");
		System.out.println(maxFlow.getFlow());
		System.out.println("Valor do fluxo máximo: " + maxFlow.getFlowValue());

	}
	
}
