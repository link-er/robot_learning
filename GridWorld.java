import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class GridWorld {
    private int[][] rewardGrid;
    private double[][] valueGrid;
    private Policy policy;
    private Random rand;

    public static enum Move {LEFT, RIGHT, DOWN, UP, DIAG_LEFT_DOWN,
         DIAG_LEFT_UP, DIAG_RIGHT_DOWN, DIAG_RIGHT_UP};

    public final int WIDTH = 7;
    public final int HEIGHT = 5;

    public GridWorld(double[] prob){
        rewardGrid = new int[HEIGHT][WIDTH];
        valueGrid = new double[HEIGHT][WIDTH];
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
    
    public void tdValue(double alpha, double gamma){
        ArrayList<Integer[]> reverseList = new ArrayList<Integer[]>();
        reverseList = GenerateEpisode();
        Collections.reverse(reverseList);
        
            double nextValue = 0.0;
            for(Integer[] elem:reverseList){
                //System.out.println(elem[2]);
                valueGrid[elem[0]][elem[1]] += alpha*(elem[2]+gamma*nextValue - valueGrid[elem[0]][elem[1]]);
                //System.out.println(valueGrid[elem[0]][elem[1]]);
                nextValue = valueGrid[elem[0]][elem[1]];
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
        for(int i = 0; i<1000;i++)
        gw.tdValue(0.1, 0.1);
        gw.printOutValueGrid();
     }
}
