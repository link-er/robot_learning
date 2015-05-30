
public class PoleCart {
    private double l = 0.75, mc = 1.0, mp = 0.5, g = 9.81;
    private double F;
    private double[] noise = {0.0001, 0.0001, 0.0001, 0.0001};
    private double[] state;
    
    public PoleCart(){
        F = 0;
        state = new double[4];
        state[0]=1;
        state[1]=-0.2;
        state[2]= -0.2;
        state[3]= 0.3;
    }
    
    public void discreteTimeSimulation(){
        double secDerPos = 0.0, secDerAngl = 0.0, prevAngle=0.0, prevAngVel=0.0, prevVel=0.0;
        for(int t=0;t<100;t++){
            prevAngle = state[2];
            prevAngVel = state[3];
            prevVel = state[1];
            secDerAngl = secondDerivAngl(prevAngle,prevAngVel);
            secDerPos = secondDerivPos(prevAngle,prevAngVel,secDerAngl);
            
            state[3]= secDerAngl*0.01+noise[3];
            state[2]=prevAngVel*0.01+noise[2];
            state[1]=secDerPos*0.01+noise[1];
            state[0]=prevVel*0.01+noise[0];
            printStateOut();
        }
    }
    
    public void printStateOut(){
        for(int i=0;i<4;i++)
            System.out.printf("%.20f ",state[i]);
        System.out.println();
    }
    
    public double secondDerivPos(double angle, double angleVel, double angleAccell){
        return (F-mp*l*(angleAccell*Math.cos(angle)-Math.pow(angleVel,2)*Math.sin(angle)))/(mc+mp);
    }
    
    public double secondDerivAngl(double angle, double angleVel){
        return (g*Math.sin(angle)*(mc+mp)-Math.cos(angle)*(F+mp*l*Math.pow(angleVel, 2)*Math.sin(angle)))/((4.0/3.0)*l*
                (mc+mp)-mp*l*Math.pow(Math.cos(angle), 2));
    }
    public static void main(String[] args){
        
        PoleCart pc = new PoleCart();
        pc.discreteTimeSimulation();
    }

}
