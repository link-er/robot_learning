package ca;

import java.util.Random;
import java.util.Scanner;

public class CA {
    static int GRID_SIZE = 84;
    static int SEED = 42;
    static int BORDER_SIZE = 2;
    static int BORDER_VALUE = 0;

    private int[] grid, arr1, rule;
    public int[] getGrid() {
        return grid;
    }

    public CA(String startingCondition) {
        arr1 = new int[84];
        setInitial(startingCondition);
    }

    //setting random values in the beginning or one seed cell
    private void setInitial(String startingCondition) {
        grid = new int[GRID_SIZE];
        switch(startingCondition) {
        case "R": {
            Random rand = new Random();
            for(int i=0; i<GRID_SIZE; i++)
                if(rand.nextDouble() < 0.5)
                    grid[i] = 0;
                else
                    grid[i] = 1;
            break;
        }
        default: {
            grid[SEED] = 1;
            break;
        }
        }
    }

    //visualizing
    public void printOut(int[] a){
        for(int i = 0; i<84; i++)
            if(a[i]==0)
                System.out.print("-");
            else
                System.out.print("*");
        System.out.println();
    }

    public void setRule(int radius, int ruleNumber) {
        //initialize rule
        int ruleLength = (int) Math.pow(2, 2*radius+1);
        rule = new int[ruleLength];
        //get binary values of rule
        String[] tmp = Integer.toBinaryString(ruleNumber).split("");
        for (int i = 0; i < tmp.length; i++)
            rule[ruleLength - tmp.length + i] = Integer.parseInt(tmp[i]);
        for(int i=0; i<rule.length/2; i++){
            int btmp = rule[ruleLength - i-1];
            rule[ruleLength - i-1] = rule[i];
            rule[i]=btmp;
        }
    }

    public int[] applyRule() {
        for (int i = 2; i < 82; i++) {
            int sum = 0;
            for (int j = 2; j >= 0; j--)
                sum += (int) (Math.pow(2, j)) * grid[i + 1 - j];
            arr1[i]=rule[sum];

        }

       for(int i=0;i<84;i++)
           grid[i]=arr1[i];
       return arr1;
    }

    public static void main(String[] args) {
        //reading parameters
        Scanner in = new Scanner(System.in);
        int r;
        while(true){
            System.out.println("Enter neighborhood radius:");
            r = in.nextInt();
            if (r>0 && r<3){
                break;
            }
        }

        int rule;
        while(true){
            System.out.println("Enter Wolfram notation for rule:");
            rule = in.nextInt();
            if (r>0 && r<256){
                break;
            }
        }

        String startingCondition;
        in.nextLine();
        while(true){
            System.out.println("Enter starting condition (R for random or S for 1 seed):");
            startingCondition = in.nextLine();
            if (startingCondition.equals("R") || startingCondition.equals("S")){
                break;
            }
        }

        in.close();

        //main action
        CA ca = new CA(startingCondition);
        ca.setRule(r, rule);
        ca.printOut(ca.getGrid());
        while(true) {
            ca.printOut(ca.applyRule());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
