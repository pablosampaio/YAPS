package tests.graph;

import java.io.IOException;

import yaps.graph_library.Graph;
import yaps.graph_library.GraphReader;
import yaps.graph_library.algorithms.EdgeConnectivity;
import yaps.graph_library.algorithms.VertexConnectivity;


public class TestConnectivity {
	
	public static void main(String[] args) throws IOException {
		Graph graph = GraphReader.readAdjacencyList("src\\tests\\graph\\grafo-11.txt"); 
		
		VertexConnectivity vc = new VertexConnectivity(graph);
		EdgeConnectivity ec = new EdgeConnectivity(graph);

		/* Resultados esperados no "grafo-11": key=2 e lambda=2
		 */
		System.out.println();
		System.out.println("KEY(G) = "    + vc.getVertexConnectivity());
		System.out.println("LAMBDA(G) = " + ec.getEdgeConnectivity());
	}
	
}
