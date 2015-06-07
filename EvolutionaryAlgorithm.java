import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class EvolutionaryAlgorithm {
    private int l, p, k, toReproduce;
    private Map<Double, double[]> population;
    private double[][] parents;
    private double[][] offspring;
    
    public EvolutionaryAlgorithm(int p, int l, int k, int toReproduce) {
        this.p = p;
        this.l = l;
        this.k = k;
        this.toReproduce = toReproduce;
        population = new HashMap<Double, double[]>();
    }
    
    public void initialiseIndividuals() {
        double[] individual = new double[l];
        double rank;
        Random rand = new Random();
        for(int i=0; i<p; i++) {
            for(int j=0; j<l; j++)
                individual[j] = rand.nextDouble();
            rank = fitness(individual);
            population.put(rank, individual);
        }
    }
    
    public void printPopulation() {
        for(Map.Entry<Double, double[]> entry : population.entrySet())
            System.out.println(Arrays.toString(entry.getValue()) + " with rank " + entry.getKey());
    }
    
    public void printParents() {
        printOutArrayOfArrays(parents);
    }
    
    public void printOffspring() {
        printOutArrayOfArrays(offspring);
    }
    
    private void printOutArrayOfArrays(double[][] array) {
        for(double[] entry : array)
            System.out.println(Arrays.toString(entry));
    }
    
    public void externalSelection(int survive) {
        double[] ranks = new double[p];
        int i = 0;
        for(Double key : population.keySet()) {
            ranks[i] = key;
            i++;
        }
        Arrays.sort(ranks);
        for(i=0; i<p-survive; i++)
            population.remove(ranks[i]);
    }
    
    public void parentSelection() {
        //TODO will be implemented - select k parents from population (only survivals there already)
        parents = new double[k][l];
        int i=0;
        for(double[] parent : population.values()) {
            parents[i] = parent;
            i++;
        }
    }
    
    public void generateOffspring() {
        //TODO will be implemented - produce toReproduce genoms from parents
        offspring = new double[toReproduce][l];
        for(int i=0; i<toReproduce; i++)
            for(int j=0; j<l; j++)
                offspring[i][j] = parents[i][j];
    }
    
    //equally distributed between -epsilon;+epsilon
    public void mutateOffspring(double epsilon) {
        Random rand = new Random();
        for(int i=0; i<toReproduce; i++) {
            for(int j=0; j<l; j++)
                offspring[i][j] += (rand.nextDouble() - 0.5)*2 * epsilon;
            //put mutated offspring to population again
            population.put(fitness(offspring[i]), offspring[i]);
        }
    }
    
    //normally distributed with params
    public void mutateOffspring(double deviation, double mean) {
        Random rand = new Random();
        for(int i=0; i<toReproduce; i++) {
            for(int j=0; j<l; j++)
                offspring[i][j] += rand.nextGaussian() * deviation + mean;
            //put mutated offspring to population again
            population.put(fitness(offspring[i]), offspring[i]);
        }
    }
    
    //continue till maximal fitness in population is less than 100
    public boolean toContinue() {
        double max = -100000;
        for(Double rank : population.keySet())
            if(rank > max)
                max = rank;
        return max < 100;
    }
    
    public double[] getOffspringRanks() {
        double[] result = new double[toReproduce];
        int i = 0;
        for(double[] child : offspring) {
            result[i] = fitness(child);
            i++;
        }
        return result;
    }
    
    //fitness is average of gens in genome
    private double fitness(double[] genome) {
        double sum = 0;
        for(double gen : genome)
            sum += gen;
        return sum/l;
    }
    
    public static void main(String[] args) {
        int p = 2;//population size
        int l = 5;//genome length
        int survive = 1;//number of pool for parents after external selection
        int parents = 1;//number of parents
        double epsilon = 0.1;//for mutation
        double deviation = 2, mean = 5;//for mutation
        
        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(p, l, parents, p-survive);
        alg.initialiseIndividuals();
        while(alg.toContinue()) {
            System.out.println("Population");
            alg.printPopulation();
            System.out.println("External selection");
            alg.externalSelection(survive);
            alg.printPopulation();
            System.out.println("Parent selection");
            alg.parentSelection();
            alg.printParents();
            System.out.println("Generate offspring");
            alg.generateOffspring();
            alg.printOffspring();
            System.out.println("Mutation");
            //alg.mutateOffspring(epsilon);
            alg.mutateOffspring(deviation, mean);
            System.out.println("Resulting fitness: " + Arrays.toString(alg.getOffspringRanks()));
            System.out.println("***");
        }
    }
}
