import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class GridWorld {
    private int[][] rewardGrid;
    private double[][] valueGrid;
    private double[][][] actionGrid;
    private Policy policy;
    private Random rand;

    public static enum Move {LEFT, RIGHT, DOWN, UP, DIAG_LEFT_DOWN,
         DIAG_LEFT_UP, DIAG_RIGHT_DOWN, DIAG_RIGHT_UP};

    public final int WIDTH = 7;
    public final int HEIGHT = 5;

    public GridWorld(double[] prob){
        rewardGrid = new int[HEIGHT][WIDTH];
        valueGrid = new double[HEIGHT][WIDTH];
        actionGrid = new double[HEIGHT][WIDTH][Move.values().length];
        policy = new Policy(prob);

        for(int i = 0; i < HEIGHT; i++)
            for(int j = 0; j < WIDTH; j++){
                rewardGrid[i][j] = -1;
                valueGrid[i][j] = 0.0;
            }
        rewardGrid[0][3] = -500; rewardGrid[0][4] = -500; rewardGrid[0][5] = -500; rewardGrid[0][6] = -500;
        rewardGrid[1][6] = -500; rewardGrid[2][5] = -500; rewardGrid[2][6] = -500; rewardGrid[3][2] = -500;
        rewardGrid[4][1] = -500; rewardGrid[4][2] = -500; rewardGrid[4][3] = -500; rewardGrid[4][4] = -500;
        rewardGrid[4][5] = -500; rewardGrid[4][6] = 1000;
    }

    public ArrayList<Integer[]> GenerateEpisode(){
        ArrayList<Integer[]> list = new ArrayList<Integer[]>();
        Move nextMove;
        int[] currentState = {2,0};
        int[] nextState;
        double devProbability;
        do {
            nextMove = policy.nextAction(currentState[0], currentState[1]);
            devProbability = rand.nextDouble();
            if(devProbability<0.5)
                nextState = move(currentState[0], currentState[1], nextMove);
            else if (devProbability>=0.5 && devProbability<0.75)
                nextState = deviationLeft(currentState[0], currentState[1], nextMove);
            else
                nextState = deviationRight(currentState[0], currentState[1], nextMove);
            //System.out.println(Arrays.toString(currentState));
            Integer[] episodeValue = {currentState[0],currentState[1],rewardGrid[nextState[0]][nextState[1]]};
            list.add(episodeValue);
            currentState[0] = nextState[0];
            currentState[1] = nextState[1];
        } while(!terminateState(nextState[0], nextState[1]));
        return list;
     }

    public void GenerateEpisodeActionValue(){
        Move nextMove;
        int[] currentState = {2,0};
        int[] nextState;
        double devProbability;
        double max;
        int maxIndex;
        
        do {
            max = -Double.MAX_VALUE;
            maxIndex=-1;
            for(int k = 0; k<8;k++)
                if(actionGrid[currentState[0]][currentState[1]][k]>max){
                    max = actionGrid[currentState[0]][currentState[1]][k];
                    maxIndex = k;
                    //System.out.println(k);
                    }
            devProbability = rand.nextDouble();
            if(devProbability<0.9)
                
                nextMove = Move.values()[maxIndex];
           
            else
                nextMove = Move.values()[rand.nextInt(8)];    
           nextState = move(currentState[0], currentState[1], nextMove);
           actionGrid[currentState[0]][currentState[1]][nextMove.ordinal()] += 0.1*(rewardGrid[nextState[0]][nextState[1]]-
                   actionGrid[currentState[0]][currentState[1]][nextMove.ordinal()]+ 0.9*max );
                  
            currentState[0] = nextState[0];
            currentState[1] = nextState[1];
        } while(!terminateState(nextState[0], nextState[1]));
    }
    
    public void printActionGrid(){
        for(int i = 0; i < HEIGHT; i++)
            for(int j = 0; j < WIDTH; j++){
                for(int k = 0; k < Move.values().length; k++)
                    System.out.printf("%8.2f ",actionGrid[i][j][k]);
                System.out.println();
                }
    }

    public void tdValue(double alpha, double gamma) {
        ArrayList<Integer[]> reverseList = new ArrayList<Integer[]>();
        reverseList = GenerateEpisode();
        Collections.reverse(reverseList);
        double nextValue = 0.0;
        for (Integer[] elem : reverseList) {
            // System.out.println(elem[2]);
            valueGrid[elem[0]][elem[1]] += alpha
                    * (elem[2] + gamma * nextValue - valueGrid[elem[0]][elem[1]]);
            // System.out.println(valueGrid[elem[0]][elem[1]]);
            nextValue = valueGrid[elem[0]][elem[1]];
        }

    }
    
    public void actionValue(){
        
        double[] Q = new double[8];
        for(int i = 0;i<8; i++)
            Q[i] = 0.0;
       // Move nextMove;
        
        int[] nextState;
        int[] nextStateDL;
        int[] nextStateDR;
        for (int i = 0; i < HEIGHT; i++)
            for (int j = 0; j < WIDTH; j++) {
                
                for (int k = 0; k < 8; k++) {
                    nextState = move(i, j, Move.values()[k]);
                    nextStateDL = deviationLeft(i, j, Move.values()[k]);
                    nextStateDR = deviationRight(i, j, Move.values()[k]);
                   if((!terminateState(i,j)))
                    Q[k] = 0.5
                            * (rewardGrid[nextState[0]][nextState[1]] + 0.5 * valueGrid[nextState[0]][nextState[1]])
                            + 0.25
                            * (rewardGrid[nextStateDL[0]][nextStateDL[1]] + 0.5 * valueGrid[nextStateDL[0]][nextStateDL[1]])
                            + 0.25
                            * (rewardGrid[nextStateDR[0]][nextStateDR[1]] + 0.5 * valueGrid[nextStateDR[0]][nextStateDR[1]]);
                    else Q[k] = 0.0;
                   actionGrid[i][j][k]=Q[k];
                  // System.out.println(actionGrid[i][j][k]);
                    System.out.println(i + " " + j + " " + Q[k]);
                }
            }

    }
    
    public void printOutValueGrid(){
        for(int i = 0; i < HEIGHT; i++)
        {
            System.out.println();
            for(int j = 0; j < WIDTH; j++){
                System.out.printf("%8.2f ",valueGrid[i][j]);
            }
    }
    }
    
    public void printOutPolicy(){
        int maxIndex;
        double max;
        for(int i = 0; i < HEIGHT; i++){
            System.out.println();
            for(int j = 0; j < WIDTH; j++){
                //System.out.println();
                max = -Double.MAX_VALUE;
                maxIndex=-1;
                for(int k = 0; k < Move.values().length; k++)
                    if(actionGrid[i][j][k]>max){
                        max = actionGrid[i][j][k];
                        maxIndex = k;
                }
                Move a = Move.values()[maxIndex];
                switch(a){
                case RIGHT: {
                    System.out.print("> ");
                    break;
                }
                case DIAG_RIGHT_DOWN: {
                    System.out.print("\\v ");
                    break;
                }
                case DIAG_RIGHT_UP: {
                    System.out.print("/^ ");
                    break;
                }
                case LEFT: {
                    System.out.print("< ");
                    break;
                }
                case DIAG_LEFT_DOWN: {
                    System.out.print("v/ ");
                    break;
                }
                case DIAG_LEFT_UP: {
                    System.out.print("^\\ ");
                    break;
                }
                case UP: {
                    System.out.print("^ ");
                    break;
                }
                case DOWN: {
                    System.out.print("v ");
                    break;
                }
                }
            }}
    }
    public boolean terminateState(int i, int j){
        if (rewardGrid[i][j] == -500 || rewardGrid[i][j] == 1000)
            return true;
        else
            return false;
     }

    public int[] deviationRight(int i, int j, Move a) {
        int[] result = { i, j };
        switch (a) {
        case RIGHT: {
            if (j != WIDTH-1)
                result[1] = j + 1;
            if (i != HEIGHT-1)
                result[0] = i + 1;
            return result;
        }
        case DIAG_RIGHT_DOWN: {
            if (i != HEIGHT-1)
                result[0] = i + 1;
            return result;
        }
        case DIAG_RIGHT_UP: {
            if (j != WIDTH-1)
                result[1] = j + 1;
            return result;
        }
        case LEFT: {
            if (j != 0)
                result[1] = j - 1;
            if (i != 0)
                result[0] = i - 1;
            return result;
        }
        case DIAG_LEFT_DOWN: {
            if (j != 0)
                result[1] = j - 1;
            return result;
        }
        case DIAG_LEFT_UP: {
            if (i != 0)
                result[0] = i - 1;
            return result;
        }
        case UP: {
            if (j != WIDTH-1)
                result[1] = j + 1;
            if (i != 0)
                result[0] = i - 1;
            return result;
        }
        case DOWN: {
            if (j != 0)
                result[1] = j - 1;
            if (i != HEIGHT-1)
                result[0] = i + 1;
            return result;
        }
        }
        return null;
    }

    public int[] deviationLeft(int i, int j, Move a) {
        int[] result = { i, j };
        switch (a) {
        case RIGHT: {
            if (j != WIDTH-1)
                result[1] = j + 1;
            if (i != 0)
                result[0] = i - 1;
            return result;
        }
        case DIAG_RIGHT_DOWN: {
            if (j != WIDTH-1)
                result[1] = j + 1;
            return result;
        }
        case DIAG_RIGHT_UP: {
            if (i != 0)
                result[0] = i - 1;
            return result;
        }
        case LEFT: {
            if (j != 0)
                result[1] = j - 1;
            if (i != HEIGHT-1)
                result[0] = i + 1;
            return result;
        }
        case DIAG_LEFT_DOWN: {
            if (i !=HEIGHT-1)
                result[1] = i + 1;
            return result;
        }
        case DIAG_LEFT_UP: {
            if (j != 0)
                result[1] = j - 1;
            return result;
        }
        case UP: {
            if (j != 0)
                result[1] = j - 1;
            if (i != 0)
                result[0] = i - 1;
            return result;
        }
        case DOWN: {
            if (j !=WIDTH-1)
                result[1] = j + 1;
            if (i != HEIGHT-1)
                result[0] = i + 1;
            return result;
        }
        }
        return null;
    }

    private int[] move(int i, int j, Move a) {
        int[] result = { i, j };
        switch (a) {
        case LEFT: {
            if (j != 0)
                result[1] = j - 1;
            return result;
        }
        case UP: {
            if (i != 0)
                result[0] = i - 1;
            return result;
        }
        case RIGHT: {
            if (j != WIDTH-1)
                result[1] = j + 1;
            return result;
        }
        case DOWN: {
            if (i != HEIGHT-1)
                result[0] = i + 1;
            return result;
        }
       case DIAG_LEFT_DOWN: {
           if (j != 0)
               result[1] = j - 1;
           if (i != HEIGHT-1)
               result[0] = i + 1;
           return result;
       }
       case DIAG_LEFT_UP: {
           if (j != 0)
               result[1] = j - 1;
           if (i != 0)
               result[0] = i - 1;
           return result;
       }
       case DIAG_RIGHT_DOWN: {
           if (j != WIDTH-1)
               result[1] = j + 1;
           if (i != HEIGHT-1)
               result[0] = i + 1;
           return result;
       }
       case DIAG_RIGHT_UP: {
           if (j != WIDTH-1)
               result[1] = j + 1;
           if (i != 0)
               result[0] = i - 1;
           return result;
       }
       }
       return result;
    }

    /*-------------------------------------Policy-------------------------------------------------*/
    class Policy{
        double moveProbability[][][];

        public Policy(double[] prob){
            moveProbability = new double [HEIGHT][WIDTH][8];
            for (int i = 0; i<HEIGHT; i++)
                for (int j = 0; j<WIDTH; j++)
                    for(int k = 0; k<8; k++)
                     moveProbability[i][j][k] = prob[k];
             rand = new Random();
         }

         public Move nextAction(int i, int j){
             double moveProb = rand.nextDouble();
             int moveIndex = -1;
             double prevProbs = 0.0;
             for(int k = 0; k<8; k++){
                 if(prevProbs <= moveProb && moveProb < prevProbs + moveProbability[i][j][k]) {
                     moveIndex = k;
                     break;
                 }
                 prevProbs += moveProbability[i][j][k];
             }

             return Move.values()[moveIndex];
         }

     }
     /*---------------------------------endPolicy------------------------------------------*/

    public static void main(String[] args){
        double[] prob = {0.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.25, 0.25};
        GridWorld gw = new GridWorld(prob);
        //gw.GenerateEpisode();
        System.out.println("-----------------------Task1----------------------------------");
        System.out.println("State-Value functions");
        for(int i = 0; i<1000;i++)
        gw.tdValue(0.1, 0.1);
        gw.printOutValueGrid();
        System.out.println();
        System.out.println("-----------------------Task2----------------------------------");
        System.out.println("Position on grid (i, j) & state-action value functions");
        gw.actionValue();
        System.out.println();
        System.out.println("-----------------------Task3----------------------------------");
        System.out.println("Action-value functions for each position");
        for(int i = 0; i< 100; i++)
        gw.GenerateEpisodeActionValue();
        gw.printActionGrid();
        gw.printOutPolicy();
        
     }
}
