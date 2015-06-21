import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

public class EA {
	private int p, m, l;
	private int N = 46;
	List<Point> data;
	// private Point[] individual;
	private Map<Double, Point[]> population;
	private ArrayList<Point[]> parents;
	private ArrayList<Point[]> offspring;

	public EA(String filename, int p, int m, int l) {
		this.p = p;
		this.m = m;
		this.l = l;
		data = new ArrayList<Point>();
		parents = new ArrayList<Point[]>();
		offspring = new ArrayList<Point[]>();
		population = new HashMap<Double, Point[]>();
		try {
			readData(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void initialiseIndividuals() {
		System.out.println("Start init indiv");
		Point[] individual = new Point[2 * N];
		double rank;
		for (int i = 0; i < p; i++) {
			individual = generateIndividual();
			System.out.println("individual:"+individual.toString());
			rank = fitness(individual);
			population.put(rank, individual);
		}
		System.out.println("end init indiv");
	}

	public Point[] generateIndividual() {
		// TODO
		// generate permutation for 1st part, than for second
		// somehow assign N to 46, datarow length, not 150
		Point[] individual = new Point[2 * N];
		int[] permutation1 = new int[N];
		int[] permutation2 = new int[N];
		permutation1 = permutation(N);
		permutation2 = permutation(N);
		for (int i = 0; i < N; i++) {
			for (Point point : data)
				if (point.getValue() == permutation1[i])
					individual[i] = point;

			// System.out.print(permutation1[i]+" ");
		}
		// System.out.println();
		for (int i = N; i < 2 * N; i++) {
			for (Point point : data)
				if (point.getValue() == permutation1[i - N])
					individual[i] = point;
		}

		return individual;
	}

	public void externalSelection() {
		double[] ranks = new double[p];
		int i = 0;
		for (Double key : population.keySet()) {
			ranks[i] = key;
			i++;
		}
		Arrays.sort(ranks);
		for (i = 0; i < p - m; i++)
			population.remove(ranks[i]);
	}

	public void parentSelection() {
		for(Entry<Double, Point[]> i:population.entrySet())
			parents.add(i.getValue());
	}
	
	 public void generateOffspring() {
	        int rand1 = 0, rand2 = 0;
	        while(offspring.size()<l){
	        	rand1 = (int)(Math.random()*m);
	        	rand2 = (int)(Math.random()*m);
	        	parents.get(rand2);
	        	crossover(parents.get(rand1),parents.get(rand2));
	        } 
	        System.out.println("Offspring size "+offspring.size());
	    }
	 
	 public void crossover(Point[] individual1, Point[] individual2){
		 Point[] child1 = new Point[2*N];
		 Point[] child2 = new Point[2*N];
		 for(int i=0; i<N;i++){
			 child1[i] = individual1[i];
			 child1[N+i] = individual2[N+i];
			 child2[i] = individual2[i];
			 child2[N+i] = individual1[N+i];
		 }
		 System.out.println("child1 "+child1.toString());
		 offspring.add(child1);
		 offspring.add(child2);	 
	 }
	 
	public void mutateOffspring() {
		int rand = (int) (Math.random() * 10);// up to 10 offsprings undergo
												// mutation
		int randIndex;
		int[] str = new int[2 * N];
		for (int i = 0; i < rand; i++) {
			randIndex = (int) (Math.random() * l);
			int index1 = (int) (Math.random() * 2 * N);
			int index2 = (int) (Math.random() * 2 * N);
			Point[] mutatedOffspring = new Point[2 * N];
			mutatedOffspring = offspring.remove(randIndex);
			for (int j = 0; j < mutatedOffspring.length; j++)
				str[j] = j + 1;
			swap(str, index1, index2);
			offspring.add(mutatedOffspring);
		}
		System.out.println(population.size());
		System.out.println(offspring.size());
		double rank = 0.0;
		for(Point[] point:offspring){
			rank = fitness(point);
			population.put(rank, point);
		}
		System.out.println();
		System.out.println(population.size());
			
	}
	 



	public int[] permutation(int size) {
		int index1, index2;
		int[] str = new int[N];
		for (int i = 0; i < size; i++)
			str[i] = i + 1;
		int rand = (int) (Math.random() * 10);
		for (int i = 0; i < rand; i++) {
			index1 = (int) (Math.random() * 46);
			index2 = (int) (Math.random() * 46);
			swap(str, index1, index2);
		}
		return str;
	}

	public void swap(int[] str, int i, int j) {
		int temp = 0;
		temp = str[i];
		str[i] = str[j];
		str[j] = temp;
	}

	public double fitness(Point[] individual) {
		
		double rank = 0.0;
		for (int i = 0; i < individual.length - 1; i++)
			for (int j = 0; j < 2; j++)
				rank += Math.abs(individual[i].getPoint()[j]
						- individual[i + 1].getPoint()[j]);
		
		return rank;
	}

	public void readData(String filename) throws FileNotFoundException {
		File file = new File(filename);
		Scanner sc = new Scanner(file);
		String dataString;
		sc.nextLine();
		sc.nextLine();
		while (sc.hasNextLine()) {
			data.add(new Point(sc.nextLine()));
		}
		sc.close();
	}

	public static void main(String[] args) throws FileNotFoundException {
		EA ea = new EA("Positions_PA-E.txt", 100, 20, 80);
		
		ea.initialiseIndividuals();
		ea.externalSelection();
		ea.parentSelection();
		ea.generateOffspring();
		ea.mutateOffspring();

	}
}

