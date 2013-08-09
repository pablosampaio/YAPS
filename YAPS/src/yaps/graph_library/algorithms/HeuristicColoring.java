package yaps.graph_library.algorithms;

import java.util.LinkedList;

import yaps.graph_library.Graph;
import yaps.util.BinHeapPQueue;
import yaps.util.PQueue;
import yaps.util.PQueueElement;


/**
 * Heuristic methods for finding "low" node-colorings of a graph. 
 * All the methods are variations of sequential coloring.
 *  
 * @author Pablo A. Sampaio
 */
public class HeuristicColoring {
	private int maxColor;
	private int[] coloring;
	
	private static int NOT_COLORED   =  0; // vertex not colored and not reached by the algorithm 
	private static int TO_BE_COLORED = -1; // vertex not colored but already reached

	
	/**
	 * Returns the total number of colors used. This is an estimate
	 * (an upper-bound, indeed) for the cromatic number of the graph. 
	 */
	public int getMaxColor() {
		return maxColor;
	}

	/**
	 * Returns the color given to a node. 
	 */
	public int getColor(int v) {
		return coloring[v];
	}	
	
	/**
	 * Coloração sequencial baseada na pesquisa em largura. <br>
	 * Retorna uma estimativa do número cromático.
	 */
	public int bfsColoring(Graph g) {
		int numVertices = g.getNumNodes();
		
		maxColor = 0;
		coloring = new int[numVertices]; 
		for (int i = 0; i < numVertices; i++) {
			coloring[i] = NOT_COLORED;
		}
		
		for (int start = 0; start < numVertices; start++) {
			if (coloring[start] == NOT_COLORED) {
				bfsColoringInternal(g, start);
			}
		}
		
		return maxColor;
	}
	
	// faz a pesquisa em largura a partir do vértice "start" 
	private void bfsColoringInternal(Graph g, int start) {
		int v;
		
		LinkedList<Integer> queue = new LinkedList<Integer>(); 
		
		queue.add(start);
		coloring[start] = TO_BE_COLORED;
		
		while (! queue.isEmpty()) {
			v = queue.pollFirst();
			
			coloring[v] = giveMinColor(v, g);
			
			if (coloring[v] > maxColor) {
				maxColor = coloring[v];
			}
			
			for (Integer neighbor : g.getSuccessors(v)) {
				if (coloring[neighbor] == NOT_COLORED) {
					queue.add(neighbor);
					coloring[neighbor] = TO_BE_COLORED;
				}
			}
		}
		
	}
	
	// Colore o vértice "v" usando números inteiros maiores que zero
	// como cores. A cor escolhida (e retornada) é o menor número que
	// não causa choque com os vizinhos
	private int giveMinColor(int v, Graph g) {
		int color = 0;
		boolean colorIsUnique;
		
		//ideia: criar array de boolean das cores usadas e escolher a menor posicao nao usada
		
		do {
			color ++;			
			colorIsUnique = true;

			for (Integer neighbor : g.getSuccessors(v)) {
				if (coloring[neighbor] == color) {
					colorIsUnique = false;
					break;
				}
			}
		
		} while (!colorIsUnique);
		
		return color;
	}
	
	/**
	 * Coloração sequencial em que são coloridos primeiro os vértices
	 * com menos vizinhos não-coloridos.
	 * 
	 * Retorna uma estimativa do número cromático.
	 */
	public int leastConstrainedFirstColoring(Graph g) {
		PQueue<NodeInfo> frontier = new BinHeapPQueue<NodeInfo>(g.getNumNodes());
		NodeInfo[] vertices = new NodeInfo[g.getNumNodes()];
		
		for (int v = 0; v < g.getNumNodes(); v++) {
			vertices[v] = new NodeInfo(v, g.getOutEdges(v).size());			
			frontier.add(vertices[v]);
		}

		coloring = new int[g.getNumNodes()]; // initialized with zero (an invalid color)
		maxColor = 0;
		
		int v;

		while (! frontier.isEmpty()) {
			v = frontier.removeMinimum().id;
			
			coloring[v] = giveMinColor(v, g);

			if (coloring[v] > maxColor) {
				maxColor = coloring[v];
			}

			for (Integer neighbor : g.getSuccessors(v)) {
				if (coloring[neighbor] == 0) {
					vertices[neighbor].remainingDegree --;
					frontier.decreaseKey(vertices[neighbor]);
				}
			}
		}
		
		return maxColor;
	}
	
	/**
	 * Coloração sequencial em que, dentre os vértices já atingidos
	 * pelo algoritmo, são coloridos primeiro aqueles com menos
	 * vizinhos não-coloridos.
	 * 
	 * Retorna uma estimativa do número cromático.
	 */
	public int leastConstrainedFirstColoringX(Graph g) {
		int numVertices = g.getNumNodes();
		
		maxColor = 0;
		coloring = new int[numVertices]; 
		
		NodeInfo[] vertices = new NodeInfo[numVertices];

		for (int v = 0; v < coloring.length; v++) {
			vertices[v] = new NodeInfo(v, g.getOutEdges(v).size());
			coloring[v] = NOT_COLORED;
		}
		
		NodeInfo start;
		int unvisited = numVertices;
		
		while (unvisited > 0) {
			start = new NodeInfo(-1, Integer.MAX_VALUE);
			
			for (int v = 0; v < g.getNumNodes(); v++) {
				if (coloring[v] == NOT_COLORED &&
						vertices[v].remainingDegree < start.remainingDegree) {
					start = vertices[v];
				}
			}
			
			unvisited = leastConstrainedFirstColoringXInternal(g, start, vertices, unvisited);
		}		
		
		return maxColor;
	}
	
	// usado por leastConstrainedFirstColoringX
	private int leastConstrainedFirstColoringXInternal(Graph g, NodeInfo start, NodeInfo[] vertices, int unvisited) {
		int v;
		PQueue<NodeInfo> frontier = new BinHeapPQueue<NodeInfo>(unvisited);
		
		frontier.add(start);
		coloring[start.id] = TO_BE_COLORED;

		while (! frontier.isEmpty()) {
			v = frontier.removeMinimum().id;
			unvisited --;
			
			coloring[v] = giveMinColor(v, g);

			if (coloring[v] > maxColor) {
				maxColor = coloring[v];
			}

			for (Integer neighbor : g.getSuccessors(v)) {
				vertices[neighbor].remainingDegree --;

				if (coloring[neighbor] == NOT_COLORED) {
					frontier.add(vertices[neighbor]);
					coloring[neighbor] = TO_BE_COLORED;

				} else if (coloring[neighbor] == TO_BE_COLORED) {
					frontier.decreaseKey(vertices[neighbor]);
					
				}
				
			}
		}
		
		return unvisited;
	}

	// classe auxiliar, faz a PQueue ordenar pela 
	// quantidade de vizinhos não-coloridos
	class NodeInfo extends PQueueElement {
		private int id;
		private int remainingDegree;
		
		NodeInfo(int v, int degree) {
			id = v;
			remainingDegree = degree;
		}

		@Override
		public int getKey() {
			return remainingDegree;
		}
	}
	
}
