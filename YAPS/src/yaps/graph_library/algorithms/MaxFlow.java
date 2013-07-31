package yaps.graph_library.algorithms;

import yaps.graph_library.*;

import java.util.LinkedList;
import java.util.List;


/**
 * Calculates the Max-flow using Edmonds-Karp algorithm.
 *  
 * @author Pablo A. Sampaio
 */
public class MaxFlow {
	// informação do grafo
	private int order;
	
	// propriedades do fluxo
	private int total_flow;
	private int[][] flow;
	private int[][] res_capacity;
	
	// propriedades usadas na pesquisa em extensão
	private boolean[] visited;
	private int[] parent;
	private int[] min_capacity;
	
	// usados para impedir que passe mais de 1 fluxo por cada vértice
	private int[] inflow;   //da para usar o proprio flow ??
	private int[] aux_inflow;	
 
	
	public MaxFlow() {

	}

	public Graph getMaxFlow() {
		return getMaxFlow(GraphDataRepr.LISTS);
	}
	
	public Graph getMaxFlow(GraphDataRepr r) {
		Graph g = new Graph(flow.length, r);
		
		for (int i = 0; i < order; i++) {
			for (int j = 0; j < order; j++) {
				if (flow[i][j] > 0) { 
					g.addEdge(i, j, flow[i][j]);
				}
			}
		}
		
		return g;
	}
	
	public double getMaxFlowValue() {
		return total_flow;
	}
	
	public int findMaxFlow(Graph g, int source, int sink) {
		return findMaxFlow(g, source, sink, false);
	}
	
	/**
	 * Runs Edmonds-Karp algorithm with O(V^3 * E) complexity, with the lengths of 
	 * the edges cast to integer and interpreted as capacities.
	 * <br><br>
	 * If "visitOnce" is set, only one augmenting path can pass per node. 
	 */
	protected int findMaxFlow(Graph g, int source, int sink, boolean visitOnce) {
		order = g.getNumNodes();
		
		total_flow = 0;
		flow = new int[order][order];
		res_capacity = new int[order][order];
		visited = new boolean[order];
		parent = new int[order];
		min_capacity = new int[order];
	
		if (visitOnce) {
			inflow = new int[order];
			aux_inflow = new int[order];
		}
 
		List<Edge> outV;
		for (int v = 0; v < order; v++) {
			outV = g.getOutEdges(v);
			for (Edge edge : outV) {
				res_capacity[v][edge.getTarget()] = (int)edge.getLength();
				//TODO: guardar listas de predecessores aqui?
			}
		}
		
		double increment;
		int v, pred;
 
		while (findPath(source, sink, visitOnce)) {
			/*System.out.print(">> ");
			printPath(source, sink);
			System.out.println();//*/
			
			increment = min_capacity[sink];
			total_flow += increment;
			
			v = sink;
			while (v != source) {
				pred = parent[v];
				flow[pred][v] += increment;
				flow[v][pred] -= increment;
				res_capacity[pred][v] -= increment;
				res_capacity[v][pred] += increment;
				if (visitOnce) {
					inflow[v] = aux_inflow[v];
				}
				v = pred;
			}
			
			// debug
			/*if (visitOnce) {
				for (int i = 0; i < inflow.length; i++)
					System.out.print(inflow[i] + " ");
				System.out.println();
			}//*/

		}
		
		return total_flow;
	}
	
	protected boolean findPath(int source, int sink, boolean visitOnce) {
		if (visitOnce) {
			return BFS_visit_once(source, sink);
		} else {
			return BFS(source, sink);
		}
	}
 
	// Breadth First Search in O(V^2)
	// TODO: melhorar usando as listas de adjacencias do grafo original
	protected boolean BFS(int source, int sink) {
		for (int i = 0; i < order; i++) {
			visited[i] = false;
			min_capacity[i] = Integer.MAX_VALUE;
		}
		
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int v;

		queue.add(source);
		visited[source] = true;
		
		while (! queue.isEmpty()) {
			v = queue.poll();
			
			for (int u = 0; u < order; u++) {
				if (!visited[u] && res_capacity[v][u] > 0) {
					min_capacity[u] = Math.min(min_capacity[v], res_capacity[v][u]);
					parent[u] = v;
					visited[u] = true;
					if (u == sink) return true;
					queue.addLast(u);
				}
			}
		}
		
		return false;
	}

	// Breadth First Search that avoids choosing a path
	// that passes through a previously visited vertex. 
	// Assumes that all arcs have unitary capacities.
	protected boolean BFS_visit_once(int source, int sink) {
		
		for (int i = 0; i < order; i++) {
			visited[i] = false;;
			min_capacity[i] = 1;
			aux_inflow[i] = inflow[i];
		}
		
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int v;

		queue.add(source);
		visited[source] = true;
		
		while (! queue.isEmpty()) {
			v = queue.poll();
			//System.out.println("Escolhido: " + v);
			
			for (int u = 0; u < order; u++) {
				if (! visited[u])
					if (aux_inflow[v] > 1  && flow[v][u] < 0) {
						//System.out.printf("   (%s,%s) - retorno\n", v, u);						
						parent[u] = v;
						visited[u] = true;
						aux_inflow[v] -= 1;
						if (u == sink) return true;
						queue.addLast(u);
					
					} else if (aux_inflow[v] <= 1 && res_capacity[v][u] > 0) {
						//System.out.printf("   (%s,%s) - adiante\n", v, u);
						parent[u] = v;
						visited[u] = true;
						aux_inflow[u] += 1;
						if (u == sink) return true;
						queue.addLast(u);
					
					}
			}
		}
		
		return false;
	}
	
	protected void printPath(int source, int dest) {
		if (dest == source) {
			System.out.print(dest);
		} else if (dest != -1) {
			printPath(source, parent[dest]);
			System.out.print("->" + dest);
		}
	}
	
}


