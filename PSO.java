import java.util.Arrays;

public class PSO {
	private int dimension;// dimension
	private int numberOfParticles;// number of particles
	private double omega=1.0;
	private double alpha=2.0;
	private double beta=2.0;
	private Particle[] particles;
	private double[] globalBestPosition;
	private double globalBestFitness;

	public PSO(int N, int P) {
		dimension = N;
		numberOfParticles = P;
		globalBestFitness = Double.MAX_VALUE;
		particles = new Particle[numberOfParticles];
		globalBestPosition = new double[dimension];
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new Particle(dimension);
			particles[i].intitializeParticle();
		}
		for (Particle particle : particles) {
			if (particle.getFitness() < globalBestFitness) {
				globalBestFitness = particle.getFitness();
				globalBestPosition = Arrays.copyOf(particle.getPosition(),
						globalBestPosition.length);
			}
		}
	}
	public boolean stopCriteria(int count){
		int stop = 100;
		System.out.println(globalBestFitness);
		return count==stop;}

	public void algorithm() {
		
		int count = 0;
		while (!stopCriteria(count)) {
			for (Particle particle : particles) {
				particle.updateVelocity(omega, alpha, beta, globalBestPosition);
				particle.updatePostion();
				particle.fitnessFunction();
			}
			for (Particle particle : particles) {
				if (particle.getFitness() < globalBestFitness) {
					globalBestFitness = particle.getFitness();
					globalBestPosition = Arrays.copyOf(particle.getPosition(),
							globalBestPosition.length);
					
				}
			}
			count++;
		}
	}

	public static void main(String[] args) {
		PSO pso = new PSO(2, 10);
		pso.algorithm();

	}
}
