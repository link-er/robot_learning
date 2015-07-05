import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class PSO {
	private int dimension;// dimension
	private int numberOfParticles;// number of particles
	private double omega = 1.0;
	private double alpha = 2.0;
	private double beta = 2.0;
	private Particle[] particles;
	private double[] globalBestPosition;
	private double globalBestFitness;

	public PSO(int N, int P, int initType) {
		dimension = N;
		numberOfParticles = P;
		globalBestFitness = Double.MAX_VALUE;
		particles = new Particle[numberOfParticles];
		globalBestPosition = new double[dimension];
		if (initType == 1)
			initialize1();
		else
			initialize2();
		for (Particle particle : particles) {
			if (particle.getFitness() < globalBestFitness) {
				globalBestFitness = particle.getFitness();
				globalBestPosition = Arrays.copyOf(particle.getPosition(),
						globalBestPosition.length);
			}
		}
	}

	public void initialize1() {
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new Particle(dimension);
			particles[i].intitializeParticle();
		}
	}

	public void initialize2() {
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new Particle(dimension);
			particles[i].intitializeParticleHyperCube();
		}
	}

	public boolean stopCriteria(int count) {
		int stop = 100;
		return count == stop;
	}

	public void algorithm() {

		int count = 0;
		while (!stopCriteria(count)) {
			for (Particle particle : particles) {
				particle.updateVelocity(omega, alpha, beta, globalBestPosition);
				particle.updatePostion(dimension);
				particle.fitnessFunction(dimension);
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
		System.out.println("Global best fitness: ");
		System.out.println(globalBestFitness);
		System.out.println("Global best Position: ");
		for (double coordinate : globalBestPosition)
			System.out.printf("%.2f ", coordinate);
		System.out.println();
	}

	public void algorithmPrintHosakiToFile() throws IOException {
		File file = new File("Hosaki.dat");
		PrintWriter pw = new PrintWriter(file);
		int count = 0;
		while (!stopCriteria(count)) {
			for (Particle particle : particles) {
				particle.updateVelocity(omega, alpha, beta, globalBestPosition);
				particle.updatePostion(dimension);
				particle.fitnessFunction(dimension);
			}
			for (Particle particle : particles) {
				if (particle.getFitness() < globalBestFitness) {
					globalBestFitness = particle.getFitness();
					globalBestPosition = Arrays.copyOf(particle.getPosition(),
							globalBestPosition.length);
				}
			}
			double[] coordinates = { 0.0, 0.0 };
			for (int i = 0; i < 8; i++) {
				coordinates = Arrays.copyOf(particles[i].getPosition(),
						dimension);
				pw.printf("%.5f  %.5f  ", coordinates[0], coordinates[1]);

			}
			count++;
			pw.println();
		}
		pw.flush();

	}

	public static void main(String[] args) throws IOException {
		int N;
		int P = 100;
		int initType;
		initType = 1;// Initialize at random
		System.out
				.println("---------------Random Initialization----------------------");
		N = 1;// univariate function
		System.out
				.println("---------------Univariate function------------------------");
		PSO pso = new PSO(N, P, initType);
		pso.algorithm();
		N = 2;// Hosaki function
		System.out
				.println("---------------Hosaki function----------------------------");
		PSO pso2 = new PSO(N, P, initType);
		pso2.algorithm();
		N = 10;// Rastrigin function
		System.out
				.println("---------------Rastrigin function-------------------------");
		PSO pso3 = new PSO(N, P, initType);
		pso3.algorithm();
		System.out
				.println("---------------Initialization Within Hypercube------------");
		initType = 2;// Initialize within small hypercube 0.05;
		N = 1;// univariate function
		System.out
				.println("---------------Univariate function------------------------");
		PSO pso4 = new PSO(N, P, initType);
		pso4.algorithm();
		N = 2;// Hosaki function
		System.out
				.println("---------------Hosaki function----------------------------");
		PSO pso5 = new PSO(N, P, initType);
		pso5.algorithm();
		PSO psoFile = new PSO(N, P, initType);
		psoFile.algorithmPrintHosakiToFile();
		N = 10;// Rastrigin function
		System.out
				.println("---------------Rastrigin function-------------------------");
		PSO pso6 = new PSO(N, P, initType);
		pso6.algorithm();
	}
}
