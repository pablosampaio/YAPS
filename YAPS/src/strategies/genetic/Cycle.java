package strategies.genetic;

import java.util.List;

import yaps.graph_library.Graph;
import yaps.graph_library.Path;
import yaps.graph_library.algorithms.AllPairsShortestPaths;


//ciclos são imutaveis, a logica da classe Solution depende dessa propriedade
public class Cycle {
	Path vertexes; // don't need to reinsert the first
	double cost;
	
	// criar um displacement aqui???
	
	Cycle(List<Integer> vertices) {
		this.vertexes = new Path(vertices);
	}
	
	// criar metodos para trocar vertices e para fazer as alteracoes das heuristicas de TSP 

	public void setCost(Graph graph) {
		AllPairsShortestPaths paths = new AllPairsShortestPaths();
		paths.computeShortestPaths(graph);
		this.vertexes = this.vertexes.expandShortestPaths(paths);
		this.cost = this.vertexes.getCost(graph);
	}
	
	public String toString() {
		return vertexes.toString() + " : " + this.cost;
	}
	
}
