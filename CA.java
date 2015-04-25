package ca;

import java.util.Random;
import java.util.Scanner;

public class CA {
    static int GRID_SIZE = 84;
    static int SEED = 42;
    static int BORDER_SIZE = 2;
    static int BORDER_VALUE = 0;

    private int[] grid, rule;
    private int radius;
    public int[] getGrid() {
        return grid;
    }

    public CA(String startingCondition, int radius, int ruleNumber) {
        this.radius = radius;
        setInitial(startingCondition);
        setRule(ruleNumber);
    }

    //setting random values in the beginning or one seed cell
    private void setInitial(String startingCondition) {
        grid = new int[GRID_SIZE];
        switch(startingCondition) {
        case "R": {
            Random rand = new Random();
            //do not set borders
            for(int i=BORDER_SIZE; i<GRID_SIZE - BORDER_SIZE; i++)
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

    private void setRule(int ruleNumber) {
        //initialize rule
        int ruleLength = (int) Math.pow(2, 2*radius+1);
        rule = new int[ruleLength];
        //get binary values of rule
        String[] tmp = Integer.toBinaryString(ruleNumber).split("");
        int j = 0;
        //need to inverse binary number
        //in order to get on 0 index of rule value for all-unset and on last index - all-set
        //all uncovered just zeroes
        for (int i = tmp.length - 1; i >= 0; i--) {
            rule[j] = Integer.parseInt(tmp[i]);
            j++;
        }
    }

    public void applyRule() {
        int currentRuleIndex;
        int[] temp = new int[GRID_SIZE];
        for (int i = BORDER_SIZE; i < GRID_SIZE - BORDER_SIZE; i++) {
            //transfer currently considered neighborhood to the index of applying rule
            currentRuleIndex = 0;
            for (int j = 0; j < 2*radius + 1; j++)
                currentRuleIndex += (int) (Math.pow(2, j)) * grid[i + radius - j];
            temp[i] = rule[currentRuleIndex];
        }
        for(int i=0; i<GRID_SIZE; i++)
            grid[i] = temp[i];
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
        CA ca = new CA(startingCondition, r, rule);
        while(true) {
            ca.printOut(ca.getGrid());
            ca.applyRule();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
