package yaps.graph_library.algorithms;

import java.util.List;

import yaps.graph_library.Edge;
import yaps.graph_library.Graph;


/**
 * Calculates the edge-connectivity of the whole graph or 
 * between any pair of nodes.
 *  
 * @author Pablo A. Sampaio
 */
public class EdgeConnectivity {
	//TODO: store results here
	
	public EdgeConnectivity() {

	}
	
	public int getEdgeConnectivity(Graph graph, int u, int v) {
		Graph gu = toUnitaryEdges(graph);
		MaxFlow flowFinder = new MaxFlow();
		return flowFinder.findMaxFlow(gu, u, v, false);
	}

	public int getEdgeConnectivity(Graph graph) {
		Graph gu = toUnitaryEdges(graph);
		MaxFlow flowFinder = new MaxFlow();
		
		int order = gu.getNumNodes();
		int minL = gu.getNumNodes();
		int l;

		for (int v = 0; v < order; v++) {
			for (int u = 0; u < order; u++) {
				if (v != u) {
					l = flowFinder.findMaxFlow(gu, v, u, false);
					if (l < minL) {
						minL = l;
					}
					//System.out.println("lambda(" + v + "," + u + ") = " + k);
				}
			}
		}
		
		return minL;
	}
	
	public double getAvgEdgeConnectivity(Graph graph) {
		Graph gu = toUnitaryEdges(graph);
		MaxFlow flowFinder = new MaxFlow();
		
		int order = gu.getNumNodes();

		int l;
		int count = 0;
		double avgL = 0.0d;

		for (int v = 0; v < order; v++) {
			for (int u = 0; u < order; u++) {
				if (v != u) {
					l = flowFinder.findMaxFlow(gu, v, u, false);
					
					avgL += l;
					count ++;
					//System.out.println("lambda(" + v + "," + u + ") = " + k);
				}
			}
		}
		
		// count should be valued (order*order - order)
		
		return avgL / count;
	}
	
	static Graph toUnitaryEdges(Graph g) {
		int numVertices = g.getNumNodes();
		Graph gu = new Graph(numVertices);

		List<Edge> outEdges;
		
		for (int v = 0; v < numVertices; v++) {
			outEdges = g.getOutEdges(v);
			for (Edge e : outEdges) {
				gu.addEdge(v, e.getTarget(), 1);
			}
		}
		
		return gu;
	}
	
}
