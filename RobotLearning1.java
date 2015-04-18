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
        int a, b, count;
        double q;
        Arm(int a, int b){
            this.a = a;
            this.b =b;
            this.q = 0.0;
            this.count = 0;
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
    
    public double Q(Arm a){
        
        a.q += a.reward();
        a.count++;
        return a.q;
    }
    
    public Arm maxQ(){
        double max = 0.0;
        double max1 = 0.0;
        double max2 = 0.0;
        Arm aMax1, aMax2, aMax;
        if(a1.q>=a2.q){
            max1 = a1.q;
            aMax1 = a1;}
        else{
            max1 = a2.q;
            aMax1 = a2;}
        if(a3.q>=a4.q){
            max2 = a3.q;
            aMax2 = a3;}
        else{
            max2 = a4.q;
            aMax2 = a4;}
        if(max1>=max2){
            max = max1;
            aMax = aMax1;}
        else{
            max = max2;
            aMax = aMax2;}
       // return max;
        return aMax;
    }
    
    public double getQ(Arm a){
        return a.q;
    }
    
    public int getCount(Arm a){
        return a.count;
    }
    
    
    
    public static void main(String[] args){
        RobotLearning1 rl = new RobotLearning1();
        System.out.println("Task 1:");
        double expVal = 0.0;
        double randExpVal = 0.0;
        expVal= rl.expectedValue(rl.getReward(rl.a1))+
                    rl.expectedValue(rl.getReward(rl.a2))+
                    rl.expectedValue(rl.getReward(rl.a3))+
                    rl.expectedValue(rl.getReward(rl.a4));
        System.out.println("Expected value: "+expVal);
        for(int i=0;i<4;i++){
            randExpVal += rl.expectedValue(rl.getReward(rl.chooseAction()));
        }
        System.out.println("Expected value if choosen randomly: "+randExpVal);

        System.out.println("--------------------------------------------------");
        System.out.println("Task 2:");
        double allRew = 0.0;
        for(int i=0;i<1000;i++){
            allRew += rl.getReward(rl.chooseAction());
        }
        System.out.println("Average reward: "+allRew/1000);
        System.out.println("--------------------------------------------------");
        System.out.println("Task 3:");
        rl.Q(rl.chooseAction());
        for(int i=0;i<1001;i++){
            
            double r = Math.random();
            if(r<0.2)
                rl.Q(rl.chooseAction());
            else
                rl.Q(rl.maxQ());
        
        if(i%100 == 0&&i!=0){
            double j = i;
            System.out.println("Step "+i);
            System.out.println("a1: "+rl.getCount(rl.a1)/j*100+"%");
            System.out.println("a2: "+rl.getCount(rl.a2)/j*100+"%");
            System.out.println("a3: "+rl.getCount(rl.a3)/j*100+"%");
            System.out.println("a4: "+rl.getCount(rl.a4)/j*100+"%");
            System.out.println("average reward 1: "+rl.getQ(rl.a1));
            System.out.println("average reward 2: "+rl.getQ(rl.a2));
            System.out.println("average reward 3: "+rl.getQ(rl.a3));
            System.out.println("average reward 4: "+rl.getQ(rl.a4));
            
            }
        }
            
    }

}
