package strategies.genetic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import yaps.graph_library.Graph;
import yaps.util.RandomUtil;

//outra ideia: calcular o melhor "displacement" de cada agente dentro de seu ciclo

public class Solution {
	private Cycle[] cycles;
	double evaluation;
	
	Solution(Cycle[] c, Graph g) {
		this.cycles = c;
		this.evaluation = -1;
		for (int i = 0; i < c.length; i++) {
			this.cycles[i].setCost(g);
		}
	}
	
	static Solution newRandomSolution(Graph g, int numCycles) {
		int numNodes = g.getNumNodes();
		int totalVertices = numNodes + RandomUtil.chooseInteger(0,numNodes/10 + 1); //up to 10% bigger
		
		List<Integer> vertices = new LinkedList<Integer>();
		for (int i = 0; i < numNodes; i++) {
			vertices.add(i);
		}
		for (int i = numNodes; i < totalVertices; i++) {
			vertices.add(RandomUtil.chooseInteger(0,numNodes));
		}
		
		//System.out.println(">> Chosen vertices:" + vertices);
		RandomUtil.shuffle(vertices);
		//System.out.println(">> Shuffled vertices:" + vertices);

		int minCycleSize = numNodes / numCycles;  //base number, may vary up to 10%
		int maxCycleSize = minCycleSize + (minCycleSize/10);
		
		Cycle[] cycles = new Cycle[numCycles];
		int usedVertices = 0;
		
		// nextCycles holds how many cycles still were not set
		for (int nextCycles = numCycles; nextCycles > 0; nextCycles --) {
			int cycleSize;
			int remainingVertices = totalVertices-usedVertices; 
			
			if (remainingVertices/nextCycles <= minCycleSize) {
				cycleSize = minCycleSize;
			} else if (remainingVertices/nextCycles >= maxCycleSize) {
				cycleSize = maxCycleSize;
			} else {
				cycleSize = minCycleSize + RandomUtil.chooseInteger(0, maxCycleSize-minCycleSize+1);
			}
			
			ArrayList<Integer> cycleVertices = new ArrayList<Integer>(cycleSize);
			for (int j = 0; j < cycleSize; j++) {
				cycleVertices.add(vertices.get(usedVertices));
				usedVertices ++;
			}
			cycles[nextCycles-1] = new Cycle(cycleVertices);
		}
		
		return new Solution(cycles, g);
	}

	
//	private static int chooseRandomVertex(List<Integer> unused, int numVertices) {
//		if (unused.size() > 0) {
//			int i = randGenerator.nextInt(unused.size());
//			return unused.remove(i);
//		} else {
//			return randGenerator.nextInt(numVertices);
//		}
//	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder("{{ ");
		for (int i = 0; i < cycles.length; i++) {
			builder.append(cycles[i].toString());
			builder.append("\n   ");
		}
		builder.append("}}");
		return builder.toString();
	}
	
}


