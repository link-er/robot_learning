import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class PoleCart {
    private double l = 0.75, mc = 1.0, mp = 0.5, g = 9.81;
    private double F;
    private double[] noise = {0.0001, 0.0001, 0.0001, 0.0001};
    private double[] state;

    public PoleCart(){
        F = 0;
        state = new double[4];
    }

    public void setToInitial() {
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
        return (Math.abs(state[0])>2.4 || Math.abs(state[2])>0.7);
    }

    public int episode(double k1, double k2, double k3, double k4){
        int t = 0;
        int reward = 0;
        while (true) {
            t++;
            calculateForce(k1, k2, k3, k4);
            calculateState();
            if (checkTarget())
                reward += 0;
            else if (checkFail()) {
                reward += -2 * (1000 - t);
                break;
            } else
                reward += -1;
        }
        return reward;
    }

    public static void main(String[] args){
        int t = 0;
        //TASK1
        PoleCart pc = new PoleCart();
        for(t=0;t<100;t++){
            pc.printStateOut(t);
            pc.calculateState();
        }
        pc.printStateOut(t);

        //TASK2
        pc.setToInitial();
        double k1 = -1, k2 = 3, k3 = -1, k4 = 2;
        //double k1=-485.44374999999945, k2=-513.5300000000018, k3=-230.34125000000085, k4=281.4124999999998;
        t = 0;
        int reward = 0;
        while (true) {
            pc.printStateOut(t);
            t++;
            pc.calculateForce(k1, k2, k3, k4);
            pc.calculateState();
            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("pole_output_2.dat", true)))) {
                out.println(t + " " + pc.state[0] + " " + pc.state[1] + " " + pc.state[2] + " " + pc.state[3]);
            }catch (IOException e) {
                e.printStackTrace();
            }
            if (pc.checkTarget())
                reward += 0;
            else if (pc.checkFail()) {
                reward += -2 * (1000 - t);
                break;
            } else
                reward += -1;
            //System.out.println(reward);
        }
        System.out.println("Final reward: "+reward);

        //TASK3
        pc.setToInitial();
        double[] gradient = new double[4];
        double epsilon = 0.8;
        double alpha = 0.001;
        double maxReward = -2000;
        double[] maxParams = new double[4];
        for(int i = 0;i<5000;i++){
            pc.setToInitial();
            reward = pc.episode(k1, k2, k3, k4);
            //System.out.println(k1+" "+k2+" "+k3+" "+k4+" "+reward);
            if(reward > maxReward) {
                maxReward = reward;
                maxParams[0] = k1;
                maxParams[1] = k2;
                maxParams[2] = k3;
                maxParams[3] = k4;
            }
            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("pole_output_3.dat", true)))) {
                out.println(i + " " + k1 + " " + k2 + " " + k3 + " " + k4 + " " + reward);
            }catch (IOException e) {
                e.printStackTrace();
            }
            pc.setToInitial();
            gradient[0] = (pc.episode(k1+epsilon, k2, k3, k4)-reward)/epsilon;
            pc.setToInitial();
            gradient[1] = (pc.episode(k1, k2+epsilon, k3, k4)-reward)/epsilon;
            pc.setToInitial();
            gradient[2] = (pc.episode(k1, k2, k3+epsilon, k4)-reward)/epsilon;
            pc.setToInitial();
            gradient[3] = (pc.episode(k1, k2, k3, k4+epsilon)-reward)/epsilon;
            k1 += alpha*gradient[0];
            k2 += alpha*gradient[1];
            k3 += alpha*gradient[2];
            k4 += alpha*gradient[3];
        }
        System.out.println(maxParams[0]+" "+maxParams[1]+" "+maxParams[2]+" "+maxParams[3]+" "+maxReward);
    }
}
