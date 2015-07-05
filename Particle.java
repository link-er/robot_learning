import java.util.Arrays;
import java.math.*;


public class Particle {
	double[] position;
	double[] velocity;
	double[] personalBest;
	double currentFitness;
	double personalBestFitness;

	public Particle(int dimension) {

		position = new double[dimension];
		velocity = new double[dimension];
		personalBest = new double[dimension];
		currentFitness = 0.0;
		personalBestFitness = Double.MAX_VALUE;

	}
	
	public void intitializeParticle(){
		for(int i=0; i<position.length; i++){
			position[i] =Math.random()*10;
			velocity[i] = -10+Math.random()*20;
			personalBest[i] = position[i];
		}
	}
	
	public void intitializeParticleHyperCube(){
		for(int i=0; i<position.length; i++){
			position[i] =Math.random()/20;
			velocity[i] = -10+Math.random()*20;
			personalBest[i] = position[i];
		}
	}
	
	
	public void updateVelocity(double w, double a, double b, double[] globalBest){
		double R = Math.random();
		for (int i = 0; i < velocity.length; i++)
			velocity[i] = w*velocity[i] + a*R*(-position[i]+personalBest[i]) + b*R*(-position[i]+globalBest[i]);	
	}

	public void updatePostion(int dimension) {
		//System.out.println(currentFitness);
		for (int i = 0; i < position.length; i++) {
			position[i] = position[i] + velocity[i];
			switch (dimension) {
			case 1: {
				if (position[i] > 10)
					position[i] = 10;
				if (position[i] < 0)
					position[i] = 0;
				break;
			}
			case 10: {
				if (position[i] > 5.12)
					position[i] = 5.12;
				if (position[i] < -5.12)
					position[i] = -5.12;
				break;
			}
			case 2: {
				if (position[i] > 10)
					position[i] = 10;
				if (position[i] < 0)
					position[i] = 0;
				break;
			}
			default:
				break;
			}
		}
	}
	
	public void fitnessFunction(int fitnessType) {
		switch (fitnessType) {
		case 1:
			currentFitness = position[0] * Math.cos(2 * position[0])
					+ position[0] * Math.sin(position[0]);
			break;
		case 10: {
			for (int i = 0; i < fitnessType; i++)
				currentFitness += Math.pow(position[i], 2)
						- 10*Math.cos(2.0 * Math.PI * position[i]);
			currentFitness += 10 * fitnessType;
			
			break;
		}
		case 2: {
			currentFitness = (1 - 8 * position[0] + 7 * Math.pow(position[0], 2)
					- (7.0/ 3.0) * Math.pow(position[0], 3) + (1.0 / 4.0)
					* Math.pow(position[0], 4))*Math.pow(position[1],2)*Math.exp(-position[1]);
			
			break;
		}
		default:
			currentFitness=0;
		//	break;
		}

		setPersonalBest(currentFitness);
	}
	
	public double[] getPosition(){
		return position;
	}
	
	public double getFitness(){
		return currentFitness;
	}
	
	public void setPersonalBest(double fitness) {
		if (personalBestFitness > fitness)
			personalBest = Arrays.copyOf(position, personalBest.length);
	}

}
