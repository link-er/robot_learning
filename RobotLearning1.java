package robotLearning1;

public class RobotLearning1 {
    Arm a1, a2, a3, a4;
    public RobotLearning1(){
        a1 = new Arm(2,3);
        a2 = new Arm(-2,1);
        a3 = new Arm(1,3);
        a4 = new Arm(0,5);
    }
    class Arm{
        int a, b;
        Arm(int a, int b){
            this.a = a;
            this.b =b;
        }
        double reward(){
            double r =  (Math.random()*(b-a)+a);
            return r;
        }
    }
    public Arm chooseAction(){
        int act = (int)(Math.random()*4 + 1.0);
        Arm action = null;
        switch(act){
        case 1:
            action = a1;
            break;
        case 2:
            action = a2;
            break;
        case 3:
            action = a3;
            break;
        case 4:
            action = a4;
            break;
            default:
                break;
        }
        return action;
    }
    
    public double getReward(Arm a){
        
        return a.reward();
    }
    
    public double expectedValue(double rew){
        return 0.25*rew;
    }
    
    public static void main(String[] args){
        RobotLearning1 rl = new RobotLearning1();
        System.out.println("Task 1:");
        double expVal = 0.0;
        expVal= rl.expectedValue(rl.getReward(rl.a1))+
                    rl.expectedValue(rl.getReward(rl.a2))+
                    rl.expectedValue(rl.getReward(rl.a3))+
                    rl.expectedValue(rl.getReward(rl.a4));
        System.out.println("Expected value: "+expVal);
        System.out.println("--------------------------------------------------");
        System.out.println("Task 2:");
        double allRew = 0.0;
        for(int i=0;i<1000;i++){
            allRew += rl.getReward(rl.chooseAction());
        }
        System.out.println("Average reward: "+allRew/1000);
        System.out.println("--------------------------------------------------");
            
    }

}
