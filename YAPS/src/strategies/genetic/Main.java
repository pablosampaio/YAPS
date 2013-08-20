package strategies.genetic;

import java.io.IOException;

import yaps.graph_library.Graph;
import yaps.graph_library.GraphFileUtil;

public class Main {

	public static void main(String[] args) throws IOException {
		Graph g = GraphFileUtil.readGraphml("..\\Experiment Setup & Analysis\\maps\\extra\\random_directed_2.graphml");
		
		for (int i = 0; i < 10; i++) {
			System.out.println(Solution.newRandomSolution(g, 8));
		}

	}

}
