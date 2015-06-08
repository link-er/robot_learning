import java.util.ArrayList;
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

    //moving from start position to terminate and collect the way
    public ArrayList<Integer[]> generateEpisode(){
        ArrayList<Integer[]> list = new ArrayList<Integer[]>();
        Move nextMove;
        int[] currentState = {2,0};
        int[] nextState;
        double devProbability;
        do {
            //get move by policy
            nextMove = policy.nextAction(currentState[0], currentState[1]);
            //check deviation probability
            devProbability = rand.nextDouble();
            if(devProbability<0.5)
                nextState = move(currentState[0], currentState[1], nextMove);
            else if (devProbability>=0.5 && devProbability<0.75)
                nextState = deviationLeft(currentState[0], currentState[1], nextMove);
            else
                nextState = deviationRight(currentState[0], currentState[1], nextMove);
            //saving step of the episode
            Integer[] episodeValue = {currentState[0],currentState[1],rewardGrid[nextState[0]][nextState[1]]};
            list.add(episodeValue);
            //moving to the next state
            currentState[0] = nextState[0];
            currentState[1] = nextState[1];
        } while(!terminateState(nextState[0], nextState[1]));
        return list;
     }

    public void qLearningEpisode(double alpha, double gamma){
        Move nextMove;
        int[] currentState = {2,0}, nextState;
        double greedyProbability, max;
        int maxIndex;

        do {
            //finding greedy action
            max = -Double.MAX_VALUE;
            maxIndex = -1;
            for(int k = 0; k<8; k++)
                if(actionGrid[currentState[0]][currentState[1]][k]>max){
                    max = actionGrid[currentState[0]][currentState[1]][k];
                    maxIndex = k;
                }
            //checking probability of greedy action
            greedyProbability = rand.nextDouble();
            if(greedyProbability < 0.9)
                nextMove = Move.values()[maxIndex];
            else
                nextMove = Move.values()[rand.nextInt(8)];
            //updating Q value accordingly to the next state
            nextState = move(currentState[0], currentState[1], nextMove);
            actionGrid[currentState[0]][currentState[1]][nextMove.ordinal()] += alpha*(rewardGrid[nextState[0]][nextState[1]]-
                   actionGrid[currentState[0]][currentState[1]][nextMove.ordinal()] + gamma*max );
            //moving to the next state
            currentState[0] = nextState[0];
            currentState[1] = nextState[1];
        } while(!terminateState(nextState[0], nextState[1]));
    }

    public void printActionGrid(){
        for(int i = 0; i < HEIGHT; i++)
            for(int j = 0; j < WIDTH; j++) {
                for(int k = 0; k < 8; k++)
                    System.out.printf("%8.2f ", actionGrid[i][j][k]);
                System.out.println();
            }
    }

    //update V values after generating an episode
    public void tdValue(double alpha, double gamma) {
        ArrayList<Integer[]> reverseList = new ArrayList<Integer[]>();
        reverseList = generateEpisode();
        Collections.reverse(reverseList);
        double nextValue = 0.0;
        for (Integer[] elem : reverseList) {
            valueGrid[elem[0]][elem[1]] += alpha
                    * (elem[2] + gamma * nextValue - valueGrid[elem[0]][elem[1]]);
            nextValue = valueGrid[elem[0]][elem[1]];
        }
    }

    //calculate by full backup
    public void calculateActionValues(){
        int[] nextState, nextStateDL, nextStateDR;
        for (int i = 0; i < HEIGHT; i++)
            for (int j = 0; j < WIDTH; j++) {
                for (int k = 0; k < 8; k++) {
                    nextState = move(i, j, Move.values()[k]);
                    nextStateDL = deviationLeft(i, j, Move.values()[k]);
                    nextStateDR = deviationRight(i, j, Move.values()[k]);
                    if(!terminateState(i,j))
                        actionGrid[i][j][k] = 0.5
                            * (rewardGrid[nextState[0]][nextState[1]] + 0.5 * valueGrid[nextState[0]][nextState[1]])
                            + 0.25
                            * (rewardGrid[nextStateDL[0]][nextStateDL[1]] + 0.5 * valueGrid[nextStateDL[0]][nextStateDL[1]])
                            + 0.25
                            * (rewardGrid[nextStateDR[0]][nextStateDR[1]] + 0.5 * valueGrid[nextStateDR[0]][nextStateDR[1]]);
                }
            }
    }

    public void printValueGrid(){
        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < WIDTH; j++)
                System.out.printf("%8.2f ",valueGrid[i][j]);
            System.out.println();
        }
    }

    public final String[] movesArrows = {"-", "-", "|", "|", "/",
            "\\", "\\", "/"};
    private String[][] policyMove(String center, String[] moves) {
        String[][] result = new String[3][3];
        result[0][0] = moves[Move.DIAG_LEFT_UP.ordinal()];
        result[0][1] = moves[Move.UP.ordinal()];
        result[0][2] = moves[Move.DIAG_RIGHT_UP.ordinal()];
        result[1][0] = moves[Move.LEFT.ordinal()];
        result[1][1] = center;
        result[1][2] = moves[Move.RIGHT.ordinal()];
        result[2][0] = moves[Move.DIAG_LEFT_DOWN.ordinal()];
        result[2][1] = moves[Move.DOWN.ordinal()];
        result[2][2] = moves[Move.DIAG_RIGHT_DOWN.ordinal()];
        return result;
    }
    public void printPolicyFromActionValues(){
        double max;
        ArrayList<String[]> row1, row2, row3;
        String[] emptyMoves = {" ", " ", " ", " ", " ", " ", " ", " "}, moves = new String[8];
        String[][] gridRow;
        for(int i = 0; i < HEIGHT; i++){
            System.out.println();
            row1 = new ArrayList<String[]>();
            row2 = new ArrayList<String[]>();
            row3 = new ArrayList<String[]>();
            for(int j = 0; j < WIDTH; j++){
                if(i==4 && j==6) {
                    gridRow = policyMove("*", emptyMoves);
                    row1.add(gridRow[0]); row2.add(gridRow[1]); row3.add(gridRow[2]);
                    continue;
                }
                if(terminateState(i,j)) {
                    gridRow = policyMove("-", emptyMoves);
                    row1.add(gridRow[0]); row2.add(gridRow[1]); row3.add(gridRow[2]);
                    continue;
                }

                max = -Double.MAX_VALUE;
                for(int k = 0; k < 8; k++)
                    if(actionGrid[i][j][k] > max)
                        max = actionGrid[i][j][k];
                for(int k = 0; k < 8; k++) {
                    if(Math.abs(actionGrid[i][j][k] - max) < 0.0001)
                        moves[k] = movesArrows[k];
                    else
                        moves[k] = " ";
                }
                gridRow = policyMove("#", moves);
                row1.add(gridRow[0]); row2.add(gridRow[1]); row3.add(gridRow[2]);
            }
            for(String[] rowPart : row1)
                for(int l=0; l<3; l++) System.out.print(rowPart[l]);
            System.out.println();
            for(String[] rowPart : row2)
                for(int l=0; l<3; l++) System.out.print(rowPart[l]);
            System.out.println();
            for(String[] rowPart : row3)
                for(int l=0; l<3; l++) System.out.print(rowPart[l]);
            System.out.println();
        }
    }

    public boolean terminateState(int i, int j) {
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
        System.out.println("-----------------------Task1----------------------------------");
        System.out.println("State-Value functions");
        for(int i = 0; i<1000; i++)
            gw.tdValue(0.1, 0.1); //alpha and gamma are params
        gw.printValueGrid();
        System.out.println();
        System.out.println("-----------------------Task2----------------------------------");
        System.out.println("Action-value functions for each position");
        gw.calculateActionValues();
        gw.printActionGrid();
        System.out.println();
        System.out.println("-----------------------Task3----------------------------------");
        System.out.println("Action-value functions for each position");
        for(int i = 0; i< 10000; i++)
            gw.qLearningEpisode(0.1, 0.1); //alpha and gamma are params
        gw.printActionGrid();
        gw.printPolicyFromActionValues();
     }
}
