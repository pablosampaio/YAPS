package yaps.util;

import java.util.List;
import java.util.Random;

import strategies.genetic.Solution;

public class RandomUtil {
	
	//TODO: usar outro gerador, porque o de Java é ruim!
	private static long SEED = 1613; 
	private static Random rand = new Random(SEED); 
	
	/**
	 * Randomly chooses an integer from the inclusive range "from .. to".  
	 */
	public static int chooseInteger(int from, int to) {
		return from + rand.nextInt(to-from+1);
	}

	/**
	 * Escolhe uma das posicoes do array aleatoriamente, com probabilidades 
	 * (para cada posição) proporcionais aos pesos delas. 
	 */
	public static int chooseProportionally(double[] weights) {
		double sum = 0.0d;
		for (int i = 0; i < weights.length; i++) {
			sum += weights[i];
		}
		
		double choice = sum * rand.nextDouble(); //choice is in interval [0;sum)
		
		double partialSum = 0.0d;
		for (int i = 0; i < weights.length; i++) {
			partialSum += weights[i];
			if (choice <= partialSum) {
				return i;
			}
		}
		
		return weights.length - 1; //não deveria acontecer!
	}

	// Fisher-Yattes algorithm
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void shuffle(List list) {
		int j;
		Object temp;
		for (int i = list.size()-1; i >= 1; i --) {
			j = rand.nextInt(i+1); //[0, i]
			temp = list.get(i);
			list.set(i, list.get(j));
			list.set(j, temp);
		}
	}

}
