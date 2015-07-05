import java.util.Arrays;


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
	
	public void updateVelocity(double w, double a, double b, double[] globalBest){
		double R = Math.random();
		for (int i = 0; i < velocity.length; i++)
			velocity[i] = w*velocity[i] + a*R*(-position[i]+personalBest[i]) + b*R*(-position[i]+globalBest[i]);	
	}

	public void updatePostion() {
		for (int i = 0; i < position.length; i++)
		{
			position[i] = position[i] + velocity[i];
			if(position[i]>10) position[i]=10;
			if(position[i]<0) position[i]=0;
		}
	}
	
	public void fitnessFunction(){
		currentFitness = position[0]*Math.cos(2*position[0])+position[0]*Math.sin(position[0]);
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
