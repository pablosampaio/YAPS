package yaps.graph_library.algorithms;

import java.util.ArrayList;
import java.util.List;

import yaps.graph_library.*;


public class BipartiteMatching {
	private int numVertices;
	private int[][] capacity;
	
	private int[] matching;
	private int totalMatches;
	

	/**
	 * Retorna o tamanho do emparelhamento máximo, desde que o
	 * método findMaxMatching() já tenha sido chamado.
	 */
	public int getMaxMatchingSize() {
		return totalMatches;
	}
	
	/**
	 * Retorna o emparelhamento máximo, desde que o método
	 * findMaxMatching() já tenha sido chamado.
	 */
	public List<Edge> getMaxMatching() {
		ArrayList<Edge> list = new ArrayList<Edge>(totalMatches);
		
		for (int v = 0; v < matching.length; v++) {
			if (matching[v] != -1) {
				list.add(new Edge(v, matching[v]));
				matching[matching[v]] = -1;  // evita duplicações
			}
		}
		
		return list;
	}
	
	/**
	 * Acha um emparelhamento máximo em um grafo bipartido. O grafo
	 * precisa ser bipartido (o método testa) e simétrico (o método
	 * não testa). 
	 */
	public int findMaxMatching(Graph graph) {
		HeuristicColoring coloring = new HeuristicColoring();
		coloring.bfsColoring(graph);
		if (coloring.getMaxColor() > 2) {
			throw new IllegalArgumentException("Graph is not bipartite.");
		}

		numVertices = graph.getNumNodes();

		// prepara o grafo criando um vértice fonte e um sumidouro e direcionado
		// as aresas na direção: fonte -> partição1 -> partição2 -> sumidouro

		int fonte = numVertices;
		int sumidouro = numVertices + 1;
		
		capacity = new int[numVertices + 2][numVertices + 2];
		
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
		
		totalMatches = 0;
		matching = new int[numVertices];
		
		for (int v = 0; v < matching.length; v++) {
			matching[v] = -1;
		}
		
		int match;
		
		// loop principal: acha caminhos aumentantes
		// e atualiza o array de capacidades
		do {
		
			match = findPath(fonte, sumidouro, new boolean[numVertices + 2], false);
			totalMatches += match;
			
		} while (match == 1);

		
		return totalMatches;
	}
	
	// procura um caminho de "atual" até "sumidouro" e retorna 1 se achar e 0, caso contrário
	// normalEdge indica se a aresta vai da partição 1 para a partição 2
	private int findPath(int atual, int sumidouro, boolean[] visited, boolean normalEdge) {
		if (atual == sumidouro) {
			//System.out.print("SUMID");
	        return 1;
		}
		
		visited[atual] = true ;
		
		int match;
		
		for (int v = 0; v < visited.length; v++) {
			
			if ( !visited[v] && capacity[atual][v] == 1 ) {
				match = findPath(v, sumidouro, visited, !normalEdge);
				
				if (match == 1) {
					capacity[atual][v] = 0;
					capacity[v][atual] = 1;
					
					// atualiza o emparelhamento somente se a aresta for uma das
					// arestas originais que saem da partição 1 para a partição 2
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
