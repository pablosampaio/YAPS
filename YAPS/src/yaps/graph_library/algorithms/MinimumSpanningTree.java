package yaps.graph_library.algorithms;

import yaps.graph_library.Edge;
import yaps.graph_library.Graph;
import yaps.graph_library.GraphDataRepr;
import yaps.util.BinHeapPQueue;
import yaps.util.PQueue;
import yaps.util.PQueueElement;


/**
 * Prim's algorithm for finding a minimum cost spanning tree of an undirected graphs.
 * 
 * @author Pablo A. Sampaio
 */
public class MinimumSpanningTree {
	private Graph graph;
	private Graph mcsTree; //the minimum-cost spanning tree
	private double mcsTreeCost;
	
	private static final int INFINITE = Integer.MAX_VALUE / 2;

	
	public MinimumSpanningTree(Graph g) {
		setGraph(g);
	}
	
	public void setGraph(Graph g) {
		this.graph = g;
		this.mcsTree = new Graph(g.getNumNodes(), GraphDataRepr.LISTS);
		this.mcsTreeCost = -1.0d;
	}

	public void calculateMinTree() {
		if (mcsTreeCost != -1.0d) {
			return;
		}
		
	    int numNodes = graph.getNumNodes();

		boolean[] chosen = new boolean[numNodes];
		PQueue<NodeInfo> frontier = new BinHeapPQueue<NodeInfo>(numNodes);
		NodeInfo[] nodeInfo = new NodeInfo[numNodes];

		int root = 0; //can be any other node...		
		for (int node = 0; node < numNodes; node ++) {
			chosen[node] = false;
			nodeInfo[node] = new NodeInfo(node, (node == root)? 0 : INFINITE);			
			frontier.add(nodeInfo[node]);
		}
		//System.out.printf("\n[PRIM, root=%d]\n", root);

		int u;
		double edgeCost;

		this.mcsTreeCost = 0.0d;
		
	    while (! frontier.isEmpty()) {
	    	u = frontier.removeMinimum().id;
	    	chosen[u] = true;
	    	//System.out.printf(" > u=%d\n", u);

	    	if (u != root) {
	    		edgeCost = nodeInfo[u].costToLink;
	    		this.mcsTree.addUndirectedEdge(u, nodeInfo[u].parent, edgeCost);
	    		this.mcsTreeCost += edgeCost;
	    		//System.out.printf("   - INSERIDA: (%d,%d), peso: %5.2f\n", nodeInfo[u].parent, u, edgeCost);
	    	}

	    	for (Edge edge : graph.getOutEdges(u)) {
	    		int neighbor = edge.getTarget();
				edgeCost = edge.getLength();

	    		if (!chosen[neighbor] && (edgeCost < nodeInfo[neighbor].costToLink)) {
	    			nodeInfo[neighbor].changeLink(u, edgeCost);
	    			frontier.decreaseKey(nodeInfo[neighbor]);
	    			//System.out.printf("   - mudou: vertice %d / peso:%5.2f\n", neighbor, edgeCost);
	    		}
			}
	    }
	    
	}
	
	public Graph getMinimumTree() {
		return this.mcsTree;				
	}
	
	public double getMinimumTreeCost() {
		return this.mcsTreeCost;
	}

	// classe auxiliar, faz a PQueue ordenar pelo
	// custo de ligar à árvore
	class NodeInfo extends PQueueElement {
		private int id;
		
		private int parent;
		private double costToLink;
		
		NodeInfo(int v, double cost) {
			id = v;
			parent = -1;
			costToLink = cost;
		}
		
		void changeLink(int parentNode, double edgeCost) {
			this.parent = parentNode;
			this.costToLink = edgeCost;
		}

		@Override
		public int getKey() {
			return (int)(100000.0 * costToLink);
		}
	}
}
