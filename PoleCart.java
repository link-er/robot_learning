
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

    public void calculateState(){
        double secDerPos = 0.0, secDerAngl = 0.0, prevAngle=0.0, prevAngVel=0.0, prevVel=0.0;
        prevAngle = state[2];
        prevAngVel = state[3];
        prevVel = state[1];
        secDerAngl = secondDerivAngl(prevAngle,prevAngVel);
        secDerPos = secondDerivPos(prevAngle,prevAngVel,secDerAngl);
        state[3] += secDerAngl*0.01 + noise[3];
        state[2] += prevAngVel*0.01 + noise[2];
        state[1] += secDerPos*0.01 + noise[1];
        state[0] += prevVel*0.01 + noise[0]; 
    }

    public void printStateOut(int t){
        System.out.print(t + " ");
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
    
    public void calculateForce(double k1,double k2, double k3, double k4){
        double policyForce = 0.0;
        policyForce = k1*state[0]+k2*state[1]+k3*state[2]+k4*state[3];
        if(policyForce>-20)
            F = policyForce;
        else
            F = -20;
        if(F>20)
            F = 20;
    }
    
    public boolean checkTarget(){
        return (Math.abs(state[0])<=0.1 && Math.abs(state[2])<=0.1);
    }
    
    public boolean checkFail(){
        return (Math.abs(state[0])>2.4 && Math.abs(state[2])>0.7);
    }
    public static void main(String[] args){
        int t = 0;
        PoleCart pc = new PoleCart();
        for(t=0;t<100;t++){
            pc.printStateOut(t);
            pc.calculateState();
    } 
        pc.printStateOut(t);
        
        PoleCart pc1 = new PoleCart();
        double k1 = 0.01, k2 = -1.0, k3 = 0.02, k4 = -0.4;
        t = 0;
        int reward = 0;
        while (true) {
            pc1.printStateOut(t);
            t++;
            pc1.calculateForce(k1, k2, k3, k4);
            pc1.calculateState();
            if (pc1.checkTarget())
                reward += 0;
            else if (pc1.checkFail()) {
                reward += -2 * (1000 - t);
                break;
            } else
                reward += -1;
            System.out.println(reward);

        }
        System.out.println("Final reward: "+reward);
    

    }}
