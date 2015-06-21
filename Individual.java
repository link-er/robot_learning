import java.util.Arrays;
import java.util.Comparator;

class SortIndividualByFitness implements Comparator<Individual> {
    //Implement Unimplemented method 
    @Override
    public int compare(Individual i1, Individual i2) {
        if(i1.getFitness() > i2.getFitness())
            return 1;
        else {
            if(i1.getFitness() < i2.getFitness())
                return -1;
            else
                return 0;
        }
    }

}

public class Individual {
    private double fitness;
    private Point[] sequence;
    
    public Individual(int n) {
        fitness = 0.0;
        // generate permutation for 1st part, then for second
        sequence = new Point[2 * n];
        int[] permutation = Helper.permutation(n);
        int permutatedIndex;
        for (Point point : Helper.data) {
            permutatedIndex = permutation[point.getValue() - 1];
            sequence[permutatedIndex - 1] = point;
        }
        permutation = Helper.permutation(n);
        for (Point point : Helper.data) {
            permutatedIndex = permutation[point.getValue() - 1];
            sequence[n + permutatedIndex - 1] = point;
        }
    }
    
    public Individual(Point[] sequence) {
        fitness = 0.0;
        this.sequence = Arrays.copyOf(sequence, sequence.length);
    }
    
    public Point[] getSequence() {
        return sequence;
    }
    
    public double getFitness() {
         return fitness;
    }

    public void evaluateFitness() {
        for (int i = 0; i < sequence.length - 1; i++)
            for (int j = 0; j < 2; j++)
                fitness += Math.abs(sequence[i].getPoint()[j]
                        - sequence[i + 1].getPoint()[j]);
    }
    
    public String toString() {
        return "Fitness " + fitness + " for " + Arrays.toString(sequence);
    }
}
