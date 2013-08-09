package yaps.graph_library;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class SubGraph extends Graph {
	private Graph superGraph;
	
	private int[] superNodesToSubNodes;
	private int[] subNodesToSuperNodes;

	//TODO: permitir subconjunto das arestas? (passar os ids)

	SubGraph(Graph g, Set<Integer> nodeSet) {
		super(nodeSet.size());
		
		this.superGraph = g;
		this.superNodesToSubNodes = new int[g.getNumNodes()];
		this.subNodesToSuperNodes = new int[nodeSet.size()];
		
		int totalSuperNodes = this.superGraph.getNumNodes();
		int totalSubNodes = 0;
		int subNode;		
		
		for (int superNode = 0; superNode < totalSuperNodes; superNode++) {
			if (nodeSet.contains(superNode)) {				
				subNode = totalSubNodes;
				totalSubNodes++;				
				this.superNodesToSubNodes[superNode] = subNode;
				this.subNodesToSuperNodes[subNode] = superNode;
			} else {
				this.superNodesToSubNodes[superNode] = -1;
				
			}
		}

		for (Integer superNode : nodeSet) {
			for (Edge e : this.superGraph.getOutEdges(superNode)) {
				int src = this.toSubNode( e.getSource() );
				int tgt = this.toSubNode( e.getTarget() );				
				if (src != -1 && tgt != -1) {
					if (e.isDirected()) {
						addEdge(src, tgt, e.getLength());
					} else if (src >= tgt) {
						addUndirectedEdge(src, tgt, e.getLength());
					}
				}
			}
		}
		
		assert(totalSubNodes == nodeSet.size());		
	}

	public Graph getSuperGraph() {
		return superGraph;
	}

	public int toSuperNode(int subGraphNode) {
		return subNodesToSuperNodes[subGraphNode];
	}
	
	public int toSubNode(int superGraphNode) {
		return superNodesToSubNodes[superGraphNode];
	}	
	
	public static void main(String[] args) throws IOException {
		Graph graph = GraphReader.readAdjacencyList("src/tests/graph/grafo-11.txt");
		
		HashSet<Integer> set = new HashSet<>();
		set.add(0); set.add(1); set.add(4); set.add(5); set.add(6);
		
		SubGraph subgraph = new SubGraph(graph, set);
		
		System.out.println("The subgraph:");
		System.out.println(subgraph);
		
		System.out.println("Node mapping (sub->super):\n");
		for (int n = 0; n < subgraph.getNumNodes(); n++) {
			System.out.printf("\t%d -> %d\n", n, subgraph.toSuperNode(n));
		}
	}
	
}
