package yaps.graph_library;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * This class represents weighted graph (network) without parallel edges.
 * <br><br>
 * Edges are of mixed type - directed or undirected. Although some operations treat the
 * edges primarily as directed (e.g. equals()). Internally, undirected edges are 
 * represented using symmetric directed edges with the same costs and identifiers, 
 * but they count as only one edge. 
 * <br><br>
 * In a graph with n nodes, nodes are represented with integers from 0 to n-1. If there 
 * are m (directed) edges, their identifiers range from 0 to m.
 * 
 * @author Pablo A. Sampaio
 */
public class Graph {
	private int numNodes;
	private int numEdges;
	
	private Edge[][]     matrix;      // adjacencies matrix
	private List<Edge>[] adjacencies; // adjacencies lists
	
	private  GraphDataRepr representation; // indicate which of the structures above are used
	
	
	public Graph(int numVertices) {
		this(numVertices, GraphDataRepr.LISTS);
	}

	@SuppressWarnings("unchecked")
	public Graph(int numVertices, GraphDataRepr r) {
		this.numNodes = numVertices;
		this.numEdges = 0;
		this.representation = (r == null) ? GraphDataRepr.LISTS : r;			
		
		if (r != GraphDataRepr.LISTS) {
			matrix = new Edge[numVertices][numVertices];
		}
		if (r != GraphDataRepr.MATRIX) {
			adjacencies = new LinkedList[numVertices];
			for (int i = 0; i < numVertices; i++) {
				adjacencies[i] = new LinkedList<Edge>();
			}
		}
	}
	
	//otimizado para: matrix, mixed
	public int addEdge(int v, int u, double length) {
		int id = this.numEdges;
		this.addDirectedEdge(id, v, u, length);
		this.numEdges ++;
		return id;
	}
	
	private void addDirectedEdge(int id, int v, int u, double length) {
		Edge edge = new Edge(id, v, u, length);
		
		if (existsEdge(v, u)) {
			throw new IllegalArgumentException("Edge from " + v + " to " + u + " already exist!");	
		}
		if (representation != GraphDataRepr.MATRIX) {
			adjacencies[v].add(edge);
		}
		if (representation != GraphDataRepr.LISTS) {
			matrix[v][u] = edge;
		}
	}

	public int addUndirectedEdge(int v, int u, double length) {
		int id = this.numEdges;
		this.addDirectedEdge(id, v, u, length);
		this.addDirectedEdge(id, u, v, length);
		this.numEdges ++;
		return id;
	}

	//otimizado para: matrix
	public void removeEdge(int v, int u) {
		if (representation != GraphDataRepr.LISTS) {
			matrix[v][u] = null;
		}
		if (representation != GraphDataRepr.MATRIX) {
			adjacencies[v].remove(new Edge(v,u));
		}		
	}

	public int getNumNodes() {
		return this.numNodes;
	}
	
	public int getNumEdges() {
		return this.numEdges;
	}
	
	//otimizado para: matrix, mixed
	public boolean existsEdge(int v, int u) {
		if (representation != GraphDataRepr.LISTS) {
			return matrix[v][u] != null;
		} else {
			return adjacencies[v].contains(new Edge(v,u));
		}
	}

	//otimizado para: matrix, mixed
	public double getLength(int source, int target) {
		if (representation != GraphDataRepr.LISTS) {
			return matrix[source][target].getLength();
		
		} else {
			Edge vu = new Edge(source,target);
			for (Edge edge : adjacencies[source]) {
				if (edge.equals(vu)) {
					return edge.getLength();
				}
			}
			return 0;
		}
	}
	
	//otimizado para: lists
	//obs.: getOutEdges is more memory-efficient e may be faster
	public List<Integer> getSuccessors(int node) {
		List<Integer> succ = new LinkedList<Integer>();
		
		if (representation != GraphDataRepr.MATRIX) {
			for (Edge e : adjacencies[node]) {
				succ.add(e.getTarget());
			}
			
		} else {
			for (int u = 0; u < matrix.length; u++) {
				if (matrix[node][u] != null) {
					succ.add(u);
				}
			}
		}
		
		return succ;
	}
	
	//otimizado para: lists 
	public List<Edge> getOutEdges(int source) {
		List<Edge> succ = new LinkedList<Edge>();
		
		if (representation != GraphDataRepr.MATRIX) {
			succ = Collections.unmodifiableList(adjacencies[source]);
			
		} else {
			for (int u = 0; u < matrix.length; u++) {
				if (matrix[source][u] != null) {
					succ.add(matrix[source][u]);
				}
			}
		}
		
		return succ;
	}

	public boolean isSymmetrical() {		
		for (int v = 0; v < getNumNodes(); v++) {
			for (Edge edge : this.getOutEdges(v)) {
				if (! existsEdge(edge.getTarget(), edge.getSource()) 
						|| getLength(edge.getTarget(), edge.getSource()) != getLength(edge.getSource(), edge.getTarget())) {
					return false;
				}
			}
		}
		
		return true;
	}

	public GraphDataRepr getRepresentation() {
		return representation;
	}
	
	@SuppressWarnings("unchecked")
	public void changeRepresentation(GraphDataRepr newRepresentation) {
		GraphDataRepr oldRepresetation = getRepresentation();
		if (oldRepresetation == newRepresentation) {
			return;
		}
		
		List<Edge>[] adj_ = null;
		Edge[][] mat_ = null;
		
		int numVertices = getNumNodes();
		List<Edge> outEdges;

		if (newRepresentation != GraphDataRepr.LISTS 
				&& oldRepresetation == GraphDataRepr.LISTS) {
			mat_ = new Edge[numVertices][numVertices];
		}
		if (newRepresentation != GraphDataRepr.MATRIX
				&& oldRepresetation == GraphDataRepr.MATRIX) {
			adj_ = new LinkedList[numVertices];
			for (int i = 0; i < numVertices; i++) {
				adj_[i] = new LinkedList<Edge>();
			}
		}
		
		for (int v = 0; v < numVertices; v++) {
			outEdges = getOutEdges(v);
			for (Edge e : outEdges) {
				if (mat_ != null) {
					mat_[v][e.getTarget()] = e;
				}
				if (adj_ != null) {
					adj_[v].add(e);
				}
			}
		}
		
		if (representation == GraphDataRepr.MATRIX) {
			this.adjacencies = null;
		}
		if (representation == GraphDataRepr.LISTS) {
			this.matrix = null;
		}
		if (adj_ != null) {
			this.adjacencies = adj_;
		}
		if (mat_ != null) {
			this.matrix = mat_;
		}
		
		this.representation = newRepresentation;
	}
	
	public boolean equals(Object o) {
		if (! (o instanceof Graph)) {
			return false;
		}
		
		Graph other = (Graph)o;
		int numVertices = this.getNumNodes();
		
		if (numVertices != other.getNumNodes()) {
			return false;
		}
		
		for (int v = 0; v < numVertices; v++) {
			for (int u = 0; u < numVertices; u++) {
				if (this.getLength(v, u) 
						!= other.getLength(v, u)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();

		if (representation != GraphDataRepr.LISTS) {
			builder.append("\n");
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix.length; j++) {
					builder.append(" ");
					builder.append(matrix[i][j]);
				}
				builder.append("\n");
			}
		
		}
		if (representation != GraphDataRepr.MATRIX) {
			builder.append("\n");
			for (int u = 0; u < adjacencies.length; u++) {
				builder.append("Adj[");
				builder.append(u);
				builder.append("] = ");
				builder.append(adjacencies[u]);
				builder.append("\n");
			}
		}
		
		return builder.toString();
	}

}
