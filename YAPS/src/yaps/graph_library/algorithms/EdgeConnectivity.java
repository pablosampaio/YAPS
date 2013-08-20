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
public class EdgeConnectivity extends GraphAlgorithm {
	//int[][] connectivity; //store results here
	private MaximumFlow flowFinder; 
	
	
	public EdgeConnectivity(Graph g) {
		super(g);		
		Graph gunitary = toUnitaryEdges(graph);
		this.flowFinder = new MaximumFlow(gunitary);
	}

	//TODO: calculate once (and store)
	//u e v tem que ser diferentes?
	public int getEdgeConnectivity(int u, int v) {
		return flowFinder.compute(u, v, false);
	}
	
	//TODO: store connectivity of all pairs
	public int getEdgeConnectivity() {
		int order = graph.getNumNodes();
		int minL = graph.getNumNodes(); //nao seria k-1 ?
		int l;

		for (int v = 0; v < order; v++) {
			for (int u = 0; u < order; u++) {
				if (v != u) {
					l = flowFinder.compute(v, u, false);
					if (l < minL) {
						minL = l;
					}
					//System.out.println("lambda(" + v + "," + u + ") = " + k);
				}
			}
		}
		
		return minL;
	}
	
	public double getAvgEdgeConnectivity() {
		int order = graph.getNumNodes();

		int l;
		int count = 0;
		double avgL = 0.0d;

		for (int v = 0; v < order; v++) {
			for (int u = 0; u < order; u++) {
				if (v != u) {
					l = flowFinder.compute(v, u, false);
					
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
				gu.addArc(v, e.getTarget(), 1);
			}
		}
		
		return gu;
	}
	
}
