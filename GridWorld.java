import java.util.Arrays;


public class GridWorld {
    public static enum Move {LEFT, UP, RIGHT, DOWN};

    private class Grid {
        private int[][] rewardGrid;
        private double[][] valueGrid;

        public Grid(double initVal) {
            rewardGrid = new int[9][9];
            valueGrid = new double[9][9];
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    rewardGrid[i][j] = -1;
            rewardGrid[1][5] = -20; rewardGrid[2][0] = 100; rewardGrid[2][1] = -20; rewardGrid[2][2] = -20;
            rewardGrid[2][3] = -20; rewardGrid[2][5] = -20; rewardGrid[2][6] = -20; rewardGrid[3][7] = -20;
            rewardGrid[4][2] = -20; rewardGrid[4][3] = -20; rewardGrid[4][4] = -20; rewardGrid[4][5] = -20;
            rewardGrid[4][7] = -20; rewardGrid[5][7] = -20; rewardGrid[6][1] = -20; rewardGrid[6][2] = -20;
            rewardGrid[6][3] = -20; rewardGrid[6][5] = -20; rewardGrid[6][6] = -20;
            for (int i = 7; i < 9; i++)
                for (int j = 1; j <= 6; j++)
                    rewardGrid[i][j] = 5;
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++) {
                    valueGrid[i][j] = initVal;
                }
        }
    }

    private class Policy {
        private double[][][] moveProbabilities;

        public Policy() {
            moveProbabilities = new double[9][9][4];
        }
        public Policy(double leftProbability, double upProbability,
                double rightProbability, double downProbability) {
            moveProbabilities = new double[9][9][4];
            for(int i=0; i<9; i++)
                for(int j=0; j<9; j++) {
                    moveProbabilities[i][j][0] = leftProbability;
                    moveProbabilities[i][j][1] = upProbability;
                    moveProbabilities[i][j][2] = rightProbability;
                    moveProbabilities[i][j][3] = downProbability;
                }
        }

        public void output() {
            Move currentMove;
            for(int i=0; i<9; i++) {
                for(int j=0; j<9; j++) {
                    if(i==2 && j==0) {
                        System.out.print("* ");
                        continue;
                    }
                    currentMove = detectMove(i, j);
                    if(currentMove == null)
                        System.out.print("_ ");
                    else
                        switch(currentMove) {
                        case LEFT: {
                            System.out.print("< ");
                            break;
                        }
                        case UP: {
                            System.out.print("^ ");
                            break;
                        }
                        case RIGHT: {
                            System.out.print("> ");
                            break;
                        }
                        case DOWN: {
                            System.out.print("v ");
                            break;
                        }
                        }
                }
                System.out.println();
            }
            System.out.println();
        }

        public Move detectMove(int i, int j) {
            if(moveProbabilities[i][j][Move.LEFT.ordinal()] == 1) return Move.LEFT;
            if(moveProbabilities[i][j][Move.UP.ordinal()] == 1) return Move.UP;
            if(moveProbabilities[i][j][Move.RIGHT.ordinal()] == 1) return Move.RIGHT;
            if(moveProbabilities[i][j][Move.DOWN.ordinal()] == 1) return Move.DOWN;
            return null;
        }
    }

    private Grid grid;
    private Policy policy;

    public GridWorld(double initVal) {
        grid = new Grid(initVal);
        policy = new Policy();
    }
    public GridWorld(double initVal, double leftProbability, double upProbability,
            double rightProbability, double downProbability) {
        grid = new Grid(initVal);
        policy = new Policy(leftProbability, upProbability, rightProbability, downProbability);
    }
    public GridWorld(double initVal, Policy policy) {
        grid = new Grid(initVal);
        this.policy = policy;
    }

    private int reward(int i, int j) {
        return grid.rewardGrid[i][j];
    }

    public double value(int i, int j) {
        return grid.valueGrid[i][j];
    }

    private double policyMove(int i, int j, Move a) {
        return policy.moveProbabilities[i][j][a.ordinal()];
    }

    private int[] move(int i, int j, Move a) {
        int[] result = new int[2];
        switch(a) {
        case LEFT: {
            if(j==0)
                result[1] = 0;
            else
                result[1] = j-1;
            result[0] = i;
            return result;
        }
        case UP: {
            if(i==0)
                result[0] = 0;
            else
                result[0] = i-1;
            result[1] = j;
            return result;
        }
        case RIGHT: {
            if(j==8)
                result[1] = 8;
            else
                result[1] = j+1;
            result[0] = i;
            return result;
        }
        case DOWN: {
            if(i==8)
                result[0] = 8;
            else
                result[0] = i+1;
            result[1] = j;
            return result;
        }
        }
        return result;
    }

    public double getStateExpectedValue(int i, int j) {
        if(i==2 && j==0)
            return 100;
        if(reward(i, j)==-20)
            return -20;

        double stateValue = 0.0;
        int[] nextState;
        int reward;
        for(Move a : Move.values()) {
            nextState = move(i, j, a);
            if(nextState[0]==i && nextState[1]==j)
                reward = -10;
            else {
                reward = reward(nextState[0], nextState[1]);
                if(reward == -20) {
                    nextState[0] = i;
                    nextState[1] = j;
                }
            }
            stateValue += policyMove(i, j, a) * (reward + 0.9 * value(nextState[0], nextState[1]));
        }
        return stateValue;
    }

    public double getStateMaximumValue(int i, int j) {
        if(i==2 && j==0)
            return 100;
        if(reward(i, j)==-20)
            return -20;

        double stateActionValue;
        int[] nextState;
        int reward;
        double maximumValue = -Double.MAX_VALUE;
        for(Move a : Move.values()) {
            nextState = move(i, j, a);
            if(nextState[0]==i && nextState[1]==j)
                reward = -10;
            else {
                reward = reward(nextState[0], nextState[1]);
                if(reward == -20) {
                    nextState[0] = i;
                    nextState[1] = j;
                }
            }
            stateActionValue = reward + 0.9 * value(nextState[0], nextState[1]);
            if(stateActionValue > maximumValue)
                maximumValue = stateActionValue;
        }
        return maximumValue;
    }

    public Policy computePolicy() {
        grid.valueGrid[2][0] = Double.MAX_VALUE;
        Policy result = new Policy();
        double maximum;
        double value;
        for(int i=0; i<9; i++)
            for(int j=0; j<9; j++) {
                maximum = Double.MIN_VALUE;
                if(i==2 && j==0)
                    continue;
                if(reward(i, j)==-20)
                    continue;

                //check neighbors
                if(j>0) {
                    value = value(i, j-1);
                    if(value > maximum) {
                        Arrays.fill(result.moveProbabilities[i][j], 0);
                        result.moveProbabilities[i][j][Move.LEFT.ordinal()] = 1;
                        maximum = value;
                    }
                }
                if(i>0){
                    value = value(i-1, j);
                    if(value > maximum) {
                        Arrays.fill(result.moveProbabilities[i][j], 0);
                        result.moveProbabilities[i][j][Move.UP.ordinal()] = 1;
                        maximum = value;
                    }
                }

                if(j<8) {
                    value = value(i, j+1);
                    if(value > maximum) {
                        Arrays.fill(result.moveProbabilities[i][j], 0);
                        result.moveProbabilities[i][j][Move.RIGHT.ordinal()] = 1;
                        maximum = value;
                    }
                }
                if(i<8) {
                    value = value(i+1, j);
                    if(value > maximum) {
                        Arrays.fill(result.moveProbabilities[i][j], 0);
                        result.moveProbabilities[i][j][Move.DOWN.ordinal()] = 1;
                        maximum = value;
                    }
                }
            }
        return result;
    }

    public void setValues(double[][] temp) {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                grid.valueGrid[i][j] = temp[i][j];
    }

    public void printValues() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                System.out.printf("%.2f ", grid.valueGrid[i][j]);
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] Args) {
        double initValue = 0.0;
        double leftProbability = 0.625;
        double upProbability = 0.125;
        double rightProbability = 0.125;
        double downProbability = 0.125;
        GridWorld gw = new GridWorld(initValue, leftProbability, upProbability, rightProbability, downProbability);
        double epsilon = 0.01;
        double[][] tempGrid = new double[9][9];
        double maximum = 0.0;
        double delta;
        //gw.printValues();
        do {
            maximum = 0;
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++) {
                    tempGrid[i][j] = gw.getStateExpectedValue(i, j);
                    delta = Math.abs(tempGrid[i][j] - gw.value(i, j));
                    if (delta > maximum)
                        maximum = delta;
                }
            gw.setValues(tempGrid);
            //gw.printValues();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        } while (maximum > epsilon);
        gw.printValues();

        GridWorld gw1 = new GridWorld(initValue, leftProbability, upProbability, rightProbability, downProbability);
        //gw1.printValues();
        do {
            maximum = 0;
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++) {
                    tempGrid[i][j] = gw1.getStateMaximumValue(i, j);
                    delta = Math.abs(tempGrid[i][j] - gw1.value(i, j));
                    if (delta > maximum)
                        maximum = delta;
                }
            gw1.setValues(tempGrid);
            //gw1.printValues();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        } while (maximum > epsilon);
        gw1.printValues();
        Policy optimal = gw1.computePolicy();
        optimal.output();

        GridWorld gw2 = new GridWorld(initValue, optimal);
        //TODO
        //make policy values to non-determenistic
        //0.6 is what now 1
        //two diagonal 0.2 depending on what is 0.6
        //gw2.printValues();
        do {
            maximum = 0;
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++) {
                    tempGrid[i][j] = gw2.getStateMaximumValue(i, j);
                    delta = Math.abs(tempGrid[i][j] - gw2.value(i, j));
                    if (delta > maximum)
                        maximum = delta;
                }
            gw2.setValues(tempGrid);
            //gw2.printValues();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        } while (maximum > epsilon);
        gw2.printValues();
        Policy optimalNonDetermenistic = gw2.computePolicy();
        optimalNonDetermenistic.output();
    }
}
