package strategies.genetic;

import java.util.List;

import yaps.graph_library.Graph;
import yaps.graph_library.PathFunctions;


//ciclos são imutaveis, a logica da classe Solution depende dessa propriedade
public class Cycle {
	List<Integer> vertexes; // don't need to reinsert the first
	double cost;
	
	// criar um displacement aqui???
	
	Cycle(List<Integer> vertices) {
		this.vertexes = vertices;
	}
	
	// criar metodos para trocar vertices e para fazer as alteracoes das heuristicas de TSP 

	public void setCost(Graph graph) {
		this.vertexes = PathFunctions.expandShortestPaths(this.vertexes, graph);
		this.cost = PathFunctions.getCost(vertexes, graph);
	}
	
	public String toString() {
		return vertexes.toString() + " : " + this.cost;
	}
}
