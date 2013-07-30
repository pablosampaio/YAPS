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
	
	private  Representation representation; // indicate which of the structures above are used
	
	
	public Graph(int numVertices) {
		this(numVertices, Representation.LISTS);
	}

	@SuppressWarnings("unchecked")
	public Graph(int numVertices, Representation r) {
		this.numNodes = numVertices;
		this.numEdges = 0;
		this.representation = (r == null) ? Representation.LISTS : r;			
		
		if (r != Representation.LISTS) {
			matrix = new Edge[numVertices][numVertices];
		}
		if (r != Representation.MATRIX) {
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
		if (representation != Representation.MATRIX) {
			adjacencies[v].add(edge);
		}
		if (representation != Representation.LISTS) {
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
		if (representation != Representation.LISTS) {
			matrix[v][u] = null;
		}
		if (representation != Representation.MATRIX) {
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
		if (representation != Representation.LISTS) {
			return matrix[v][u] != null;
		} else {
			return adjacencies[v].contains(new Edge(v,u));
		}
	}

	//otimizado para: matrix, mixed
	public double getLength(int source, int target) {
		if (representation != Representation.LISTS) {
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
		
		if (representation != Representation.MATRIX) {
			for (Edge e : adjacencies[node]) {
				succ.add(e.getTargetId());
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
		
		if (representation != Representation.MATRIX) {
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
				if (! existsEdge(edge.getTargetId(), edge.getSourceId()) 
						|| getLength(edge.getTargetId(), edge.getSourceId()) != getLength(edge.getSourceId(), edge.getTargetId())) {
					return false;
				}
			}
		}
		
		return true;
	}

	public Representation getRepresentation() {
		return representation;
	}
	
	@SuppressWarnings("unchecked")
	public void changeRepresentation(Representation newRepresentation) {
		Representation oldRepresetation = getRepresentation();
		if (oldRepresetation == newRepresentation) {
			return;
		}
		
		List<Edge>[] adj_ = null;
		Edge[][] mat_ = null;
		
		int numVertices = getNumNodes();
		List<Edge> outEdges;

		if (newRepresentation != Representation.LISTS 
				&& oldRepresetation == Representation.LISTS) {
			mat_ = new Edge[numVertices][numVertices];
		}
		if (newRepresentation != Representation.MATRIX
				&& oldRepresetation == Representation.MATRIX) {
			adj_ = new LinkedList[numVertices];
			for (int i = 0; i < numVertices; i++) {
				adj_[i] = new LinkedList<Edge>();
			}
		}
		
		for (int v = 0; v < numVertices; v++) {
			outEdges = getOutEdges(v);
			for (Edge e : outEdges) {
				if (mat_ != null) {
					mat_[v][e.getTargetId()] = e;
				}
				if (adj_ != null) {
					adj_[v].add(e);
				}
			}
		}
		
		if (representation == Representation.MATRIX) {
			this.adjacencies = null;
		}
		if (representation == Representation.LISTS) {
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

		if (representation != Representation.LISTS) {
			builder.append("\n");
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix.length; j++) {
					builder.append(" ");
					builder.append(matrix[i][j]);
				}
				builder.append("\n");
			}
		
		}
		if (representation != Representation.MATRIX) {
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
