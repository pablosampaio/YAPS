package yaps.graph_library.algorithms;

import yaps.graph_library.Graph;


/**
 * Calculates the node-connectivity (vertex-connectivity) of the 
 * whole graph or between any pair of nodes.
 *  
 * @author Pablo A. Sampaio
 */
public class VertexConnectivity {
	//TODO: store results here

	public VertexConnectivity() {

	}
	
	public int getVertexConnectivity(Graph graph, int u, int v) {
		Graph gu = EdgeConnectivity.toUnitaryEdges(graph);
		MaxFlow flowFinder = new MaxFlow();
		return flowFinder.findMaxFlow(gu, u, v, true);
	}

	public int getVertexConnectivity(Graph graph) {
		Graph gu = EdgeConnectivity.toUnitaryEdges(graph);
		MaxFlow flowFinder = new MaxFlow();
		
		int order = gu.getNumNodes();
		int minK = gu.getNumNodes();
		int k;

		for (int v = 0; v < order; v++) {
			for (int u = 0; u < order; u++) {
				if (v != u) {
					k = flowFinder.findMaxFlow(gu, v, u, true);
					if (k < minK) {
						minK = k;
					}
					//System.out.println("k(" + v + "," + u + ") = " + k);
				}
			}
		}
		
		return minK;
	}
	
	public double getAvgVertexConnectivity(Graph graph) {
		Graph gu = EdgeConnectivity.toUnitaryEdges(graph);
		MaxFlow flowFinder = new MaxFlow();
		
		int order = gu.getNumNodes();

		int k;		
		int count = 0;
		double avgK = 0.0d;
		
		for (int v = 0; v < order; v++) {
			for (int u = 0; u < order; u++) {
				if (v != u) {
					k = flowFinder.findMaxFlow(gu, v, u, true);
					
					avgK += k;
					count ++;
					//System.out.println("k(" + v + "," + u + ") = " + k);
				}
			}
		}
		
		// count should be valued (order*order - order) 		
		return avgK / count;
	}
	
}
