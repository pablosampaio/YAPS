package yaps.graph_library.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yaps.graph_library.Edge;
import yaps.graph_library.Graph;


/**
 * Finds the maximum matching (a set of pairwise non-incident edges) 
 * in a bipartite graph.
 *  
 * @author Pablo A. Sampaio
 */
public class MaximumBipartiteMatching {
	private Graph graph;
	List<Edge> matching;
	int numMatches;
	
	public MaximumBipartiteMatching(Graph g) {
		this.graph = g;
	}

	/**
	 * Retorna o tamanho do emparelhamento m�ximo, desde que o
	 * m�todo findMaxMatching() j� tenha sido chamado.
	 */
	public int getMatchingSize() {
		return numMatches;
	}
	
	/**
	 * Retorna o emparelhamento m�ximo, desde que o m�todo
	 * findMaxMatching() j� tenha sido chamado.
	 */
	public List<Edge> getMatching() {
		return this.matching;
	}
	
	/**
	 * Acha um emparelhamento m�ximo em um grafo bipartido. O grafo
	 * precisa ser bipartido (o m�todo testa) e sim�trico (o m�todo
	 * n�o testa). 
	 */
	public void compute() {
		HeuristicColoring coloring = new HeuristicColoring();		
		coloring.bfsColoring(graph);
		if (coloring.getMaxColor() > 2) {
			throw new IllegalArgumentException("Graph is not bipartite.");
		}
		
		int numNodes = this.graph.getNumNodes();
		int source = numNodes;
		int sink   = numNodes + 1;
		
		Graph augmentedGraph = new Graph(numNodes+2);
		
		for (int node = 0; node < numNodes; node++) {
			if (coloring.getColor(node) == 1) {
				augmentedGraph.addEdge(source, node, 1);			
				for (Edge e : this.graph.getOutEdges(node)) {
					augmentedGraph.addEdge(node, e.getTarget(), 1);
				}
			} else {
				augmentedGraph.addEdge(node, sink, 1);
			}
		}
		
		MaximumFlow maxFlow = new MaximumFlow(augmentedGraph);
		
		maxFlow.compute(source, sink);

		this.numMatches = maxFlow.getFlowValue();
		Graph flowGraph = maxFlow.getFlow();

		List<Edge> matchs = new ArrayList<>(this.numMatches);
		int target;
		for (int node = 0; node < numNodes; node++) {
			for (Edge e : flowGraph.getOutEdges(node)) {
				target = e.getTarget();
				if (target != sink) {
					matchs.add(new Edge(node, e.getTarget()));
				}
			}
		}
		this.matching = Collections.unmodifiableList(matchs);
	}

	/**
	 * An alternative, probably faster, implementation.
	 */
	public void computeAlternative() {
		HeuristicColoring coloring = new HeuristicColoring();
		coloring.bfsColoring(graph);
		if (coloring.getMaxColor() > 2) {
			throw new IllegalArgumentException("Graph is not bipartite.");
		}

		int numNodes = graph.getNumNodes();
		int[][] capacity;

		// prepara o grafo criando um v�rtice fonte e um sumidouro e direcionado
		// as aresas na dire��o: fonte -> parti��o1 -> parti��o2 -> sumidouro

		int fonte = numNodes;
		int sumidouro = numNodes + 1;
		
		capacity = new int[numNodes + 2][numNodes + 2];
		
		for (int v = 0; v < graph.getNumNodes(); v++) {
			
			if (coloring.getColor(v) == 1) {
				capacity[fonte][v] = 1;
				
				for (Edge e : graph.getOutEdges(v)) {
					capacity[v][e.getTarget()] = 1;
				}
				
			} else {
				capacity[v][sumidouro] = 1;

			}

		}
		
		numMatches = 0;
		int[] matching = new int[numNodes];
		
		for (int v = 0; v < matching.length; v++) {
			matching[v] = -1;
		}
		
		int match;
		
		// loop principal: acha caminhos aumentantes
		// e atualiza o array de capacidades
		do {		
			match = findPath(fonte, sumidouro, capacity, matching, new boolean[numNodes + 2], false);
			numMatches += match;			
		} while (match == 1);
		
		ArrayList<Edge> list = new ArrayList<Edge>(numMatches);
		
		for (int v = 0; v < matching.length; v++) {
			if (matching[v] != -1) {
				list.add(new Edge(v, matching[v]));
				matching[matching[v]] = -1;  // evita duplica��es
			}
		}
		assert(list.size() == numMatches);

		this.matching = Collections.unmodifiableList(list);
	}
	
	// procura um caminho de "atual" at� "sumidouro" e retorna 1 se achar e 0, caso contr�rio
	// normalEdge indica se a aresta vai da parti��o 1 para a parti��o 2
	private int findPath(int atual, int sumidouro, int[][]capacity, int[] matching, 
			boolean[] visited, boolean normalEdge) {
		if (atual == sumidouro) {
	        return 1;
		}
		
		visited[atual] = true;
		
		int match;
		
		for (int v = 0; v < visited.length; v++) {
			
			if (!visited[v] && capacity[atual][v] == 1) {
				match = findPath(v, sumidouro, capacity, matching, visited, !normalEdge);
				
				if (match == 1) {
					capacity[atual][v] = 0;
					capacity[v][atual] = 1;
					
					// atualiza o emparelhamento somente se a aresta for uma das
					// arestas originais que saem da parti��o 1 para a parti��o 2
					if (normalEdge) {
						matching[atual] = v;
						matching[v] = atual;
					}

					//System.out.printf(" <- %s", (atual == numVertices) ? "FONTE\n" : atual);
					return 1;
				}
			}
			
		}

		return 0;
	}
	
}
