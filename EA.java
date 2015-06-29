import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class EA {
	private int p, m, l, n;
	private Individual[] population;
	private Individual[] parents;
	private ArrayList<Point[]> offspring;

	public EA(int p, int n, int m, int l) {
	    //population number
		this.p = p;
	    //genome length
        this.n = n;
		//parents number
		this.m = m;
		//offspring number
		this.l = l;
		parents = new Individual[m];
		offspring = new ArrayList<Point[]>();
		population = new Individual[p];
        initialiseIndividuals();
	}

	private void initialiseIndividuals() {
	    Individual individual;
		for (int i = 0; i < p; i++) {
			individual = new Individual(n);
			population[i] = individual;
		}
	}

	public void fitnessEvaluation() {
	    for(int i=0; i<p; i++)
	        population[i].evaluateFitness();
	}

	public void externalSelection() {
	    SortIndividualByFitness varSortIndividualByFitness = new SortIndividualByFitness();
	    Arrays.sort(population, varSortIndividualByFitness);
		for (int i = 0; i < l; i++)
			population[i] = null;
	}

	public void parentSelection() {
	    int j=0;
		for(int i=0; i<p; i++) {
		    if(population[i] != null) {
		        parents[j] = population[i];
		        j++;
		    }
		}
	}

	public void inheritance() {
	    int rand1 = 0, rand2 = 0;
	    while(offspring.size() < l) {
	        rand1 = (int)(Math.random()*m);
	        rand2 = (int)(Math.random()*m);
	        crossover(parents[rand1].getSequence(), parents[rand2].getSequence());
	    }
	}

	public void crossover(Point[] parent1, Point[] parent2){
		Point[] child1 = new Point[2*n];
		Point[] child2 = new Point[2*n];
		for(int i=0; i<n; i++){
			child1[i] = parent1[i];
			child1[n+i] = parent2[n+i];
			child2[i] = parent2[i];
			child2[n+i] = parent1[n+i];
		}
		offspring.add(child1);
		offspring.add(child2);
	}

	public void mutateOffspring() {
		int rand = (int) (Math.random() * (l/4));// up to 25% of offsprings undergo mutation
		int randIndex;
		int[] str = new int[2 * n];
		for (int i = 0; i < rand; i++) {
			randIndex = (int) (Math.random() * l);
			int index1 = (int) (Math.random() * 2 * n);
			int index2 = (int) (Math.random() * 2 * n);
			Point[] mutatedOffspring = new Point[2 * n];
			mutatedOffspring = offspring.remove(randIndex);
			for (int j = 0; j < mutatedOffspring.length; j++)
				str[j] = j + 1;
			Helper.swap(str, index1, index2);
			offspring.add(mutatedOffspring);
		}
	}

	public void completePopulation() {
        for(int i=0; i<l; i++)
            population[i] = new Individual(offspring.get(i));
	}

	public void printPopulation() {
        System.out.println("Population: ");
	    for (int i = 0; i < p; i++) {
	        if(population[i] != null)
	            System.out.println(population[i].toString());
	    }
	}

	public static void main(String[] args) throws FileNotFoundException {
	    Scanner in = new Scanner(System.in);
        int P, mu;
        while(true){
            System.out.println("Enter P number of individuals in population");
            P = in.nextInt();
            if (P > 0) break;
        }
        while(true){
            System.out.println("Enter number of parents");
            mu = in.nextInt();
            if (mu > 0 && mu < P) break;
        }
        in.close();

        Helper.readData("Positions_PA-E.txt");

		EA ea = new EA(P, Helper.data.size(), mu, P-mu);
		for(int i=0; i<100; i++) {
            System.out.println("---------Step " + i + "--------");
            ea.fitnessEvaluation();
            ea.printPopulation();
            ea.externalSelection();
    		ea.parentSelection();
    		ea.inheritance();
    		ea.mutateOffspring();
            ea.completePopulation();
		}
	}
}

