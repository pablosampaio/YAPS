package yaps.graph_library;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import yaps.graph_library.algorithms.AllPairsShortestPaths;


/**
 * Auxiliary methods related to paths. 
 *  
 * @author Pablo A. Sampaio
 */
public class PathFunctions {

	/**
	 * Calculates the cost of the path in the given graph. 
	 * Returns -1 if it is not a valid path in the graph.
	 */
	public static int getCost(List<Integer> path, Graph graph) {
		int cost = 0;
		
		for (int i = 1; i < path.size(); i++) {
			if (! graph.existsEdge(path.get(i-1), path.get(i)) ) {
				System.err.println(">> não existe aresta (" + path.get(i-1) + ", " + path.get(i) + ")!");
				return -1;
			}
			cost += graph.getLength(path.get(i-1), path.get(i));
		}
		
		return cost;
	}
	
	/**
	 * Calculates the cost of the path in the given graph, considering
	 * that each (u,v) edge has the cost of the shortest path from u to v.
	 * Returns -1 if one the paths is not possible in the graph (e.g. if 
	 * the graph is not strongly connected).
	 * <br><br>
	 * It should return the same value as: <br>
	 * getCost( extractShortestPaths(path,graph) , graph) .
	 */
	public static double getCostOfShortestPaths(List<Integer> path, Graph graph) {
		AllPairsShortestPaths shortest = new AllPairsShortestPaths();
		
		shortest.computeShortestPaths(graph);
		
		int n = path.size();
		int totalCost = 0;
		double distance;
		
		for (int i = 1; i < n; i++) {
			distance = shortest.getDistance(path.get(i-1), path.get(i));
			
			if (distance == Integer.MAX_VALUE) {
				System.err.println(">> não existe caminho de " + path.get(i-1) + " a " + path.get(i) + "!");
				return - 1;
			}
			
			totalCost += distance;
		}

		return totalCost;
	}
	
	/**
	 * Expand the given path by exchanging each edge (u,v) by the shortest 
	 * path from u to v. 
	 * Returns null if one of the paths is not possible in the graph (e.g. if 
	 * the graph is not strongly connected).
	 */
	public static List<Integer> expandShortestPaths(List<Integer> path, Graph graph) {
		AllPairsShortestPaths shortest = new AllPairsShortestPaths();
		List realPath = new LinkedList<>();		
		
		shortest.computeShortestPaths(graph);
		
		realPath.add(path.get(0));

		List<Integer> partialPath;

		for (int i = 1; i < path.size(); i++) {
			partialPath = shortest.getPath(path.get(i-1), path.get(i)).toVertexList();
			
			if (partialPath == null) {
				System.err.println(">> não existe caminho de " + path.get(i-1) + " a " + path.get(i) + "!");
				return null;
			}
			
			partialPath.remove(0);
			realPath.addAll(partialPath);
		}
		
		return realPath;
	}	
	
	/**
	 * If the start and end vertex of the list are the same, rotates the
	 * list to start with the given vertex. Otherwise, returns null.
	 */
	public static List<Integer> rotateList(List<Integer> path, int start) {
		if (path.get(0) != path.get(path.size()-1)) {
			return null;
		}
		
		// removes the last, which is a repetition of the first
		path.remove(path.size() - 1);

		int startIndex = path.indexOf(start);
		List<Integer> newPath = new LinkedList<Integer>();
		
		for (int i = startIndex; i < path.size(); i++) {
			newPath.add(path.get(i));
		}
		
		// vertex start is reinserted in the end
		for (int i = 0; i <= startIndex; i++) {
			newPath.add(path.get(i));
		}
		
		return newPath;
	}
	
}
