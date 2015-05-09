import java.util.ArrayList;
import java.util.Random;


public class GridWorld {
     int[][] rewardGrid;
     double[][] valueGrid;
     Policy policy;
     double[] moveProb;
     Random rand;
     ArrayList<Integer[]> stateList;
     public static enum Move {LEFT, RIGHT, DOWN, UP, DIAG_LEFT_DOWN,
         DIAG_LEFT_UP, DIAG_RIGHT_DOWN, DIAG_RIGHT_UP};
     int WIDTH = 7;
     int HEIGHT = 5;
     
     public GridWorld(double[] prob){
         rewardGrid = new int[HEIGHT][WIDTH];
         valueGrid = new double[HEIGHT][WIDTH];
         policy = new Policy(prob);
         
        
         for(int i = 0; i < HEIGHT; i++)
             for(int j = 0; j < WIDTH; j++){
                 rewardGrid[i][j] = 0;
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
         do{
             nextMove = policy.nextAction(currentState[0], currentState[1]);
             if(rand.nextDouble()<0.5)
                 nextState = move(currentState[0], currentState[1], nextMove);
             else if (rand.nextDouble()>=0.5 && rand.nextDouble()<0.75)
                 nextState = deviationLeft(currentState[0], currentState[1], nextMove);   
             else
                 nextState = deviationRight(currentState[0], currentState[1], nextMove);
             Integer[] state = {currentState[0],currentState[1],rewardGrid[currentState[0]][currentState[1]]};
             list.add(state);
             } while(!terminateState(nextState[0], nextState[1]));
         return list;
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
            if (j != 8) {
                result[1] = j + 1;
                if (i != 8)
                    result[0] = i + 1;
            }
            return result;
        }
        case DIAG_RIGHT_DOWN: {
            if (i != 8)
                result[0] = i + 1;
            return result;
        }
        case DIAG_RIGHT_UP: {
            if (j != 8)
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
            if (j != 8) {
                result[1] = j + 1;
                if (i != 0)
                    result[0] = i - 1;
            }
            return result;
        }
        case DIAG_RIGHT_DOWN: {
            if (j != 8)
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
             if (j != 8)
                 result[1] = j + 1;
             return result;
         }
         case DOWN: {
             if (i != 8)
                 result[0] = i + 1;
             return result;
         }
        case DIAG_LEFT_DOWN: {
            if (j != 0) {
                result[1] = j - 1;
                if (i != 8)
                    result[0] = i + 1;
            }
            return result;
        }
        case DIAG_LEFT_UP: {
            if (j != 0) {
                result[1] = j - 1;
                if (i != 0)
                    result[0] = i - 1;
            }
            return result;
        }
        case DIAG_RIGHT_DOWN: {
            if (j != 8) {
                result[1] = j + 1;
                if (i != 8)
                    result[0] = i + 1;
            }
            return result;
        }
        case DIAG_RIGHT_UP: {
            if (j != 8) {
                result[1] = j + 1;
                if (i != 0)
                    result[0] = i - 1;
            }
            return result;
        }
         }
         return result;
     }

    
     
    
     
     /*-------------------------------------Policy-------------------------------------------------*/
     class Policy{
         double moveProbability[][][];
         public Policy(double[] prob){
             moveProbability = new double [5][7][8];
             for (int i = 0; i<moveProbability.length; i++)
                 for (int j = 0; j<moveProbability[0].length; j++)
                     for(int k = 0; k<moveProbability[0][0].length; k++)
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
             if(moveIndex==Move.DIAG_LEFT_DOWN.ordinal())
                 return Move.DIAG_LEFT_DOWN;
             if(moveIndex==Move.DIAG_LEFT_UP.ordinal())
                 return Move.DIAG_LEFT_UP;
             if(moveIndex==Move.DIAG_RIGHT_DOWN.ordinal())
                 return Move.DIAG_RIGHT_DOWN;
             if(moveIndex==Move.DIAG_RIGHT_UP.ordinal())
                 return Move.DIAG_RIGHT_UP;
             if(moveIndex==Move.DOWN.ordinal())
                 return Move.DOWN;
             if(moveIndex==Move.LEFT.ordinal())
                 return Move.LEFT;
             if(moveIndex==Move.UP.ordinal())
                 return Move.UP;
             
             return null;
         }
         
     }
     /*---------------------------------endPolicy------------------------------------------*/
     public static void main(String[] args){
         double[] prob = {0.0, 0.0, 0.25, 0.5, 0.25, 0.0, 0.0, 0.0};
         GridWorld gw = new GridWorld(prob);
        gw.GenerateEpisode();
                    
       
         
     }
}
