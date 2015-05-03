import java.util.Random;

public class GridWorld {
    private Grid grid;

    public GridWorld(double initVal) {
        grid = new Grid(initVal);

    }

    private class Grid {

        private int[][] rewardGrid;
        private double[][] valueGrid;

        public Grid(double initVal) {
            rewardGrid = new int[9][9];
            valueGrid = new double[9][9];
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    rewardGrid[i][j] = -1;
            rewardGrid[1][5] = -20;
            rewardGrid[2][0] = 100;
            rewardGrid[2][1] = -20;
            rewardGrid[2][2] = -20;
            rewardGrid[2][3] = -20;
            rewardGrid[2][5] = -20;
            rewardGrid[2][6] = -20;
            rewardGrid[3][7] = -20;
            rewardGrid[4][2] = -20;
            rewardGrid[4][3] = -20;
            rewardGrid[4][4] = -20;
            rewardGrid[4][5] = -20;
            rewardGrid[4][7] = -20;
            rewardGrid[5][7] = -20;
            rewardGrid[6][1] = -20;
            rewardGrid[6][2] = -20;
            rewardGrid[6][3] = -20;
            rewardGrid[6][5] = -20;
            rewardGrid[6][6] = -20;
            for (int i = 7; i < 9; i++)
                for (int j = 1; j < 6; j++)
                    rewardGrid[i][j] = 5;
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    valueGrid[i][j] = initVal;
        }

    }

    public int reward(int i, int j) {
        return grid.rewardGrid[i][j];
    }

    public double value(int i, int j) {
        return grid.valueGrid[i][j];
    }

    public double getStateExpectedValue(int i, int j) {
        double stateValue = 0.0;
        stateValue = 0.625 * (reward(i, j - 1) + 0.9 * value(i, j - 1)) + 0.125
                * (reward(i, j + 1) + 0.9 * value(i, j + 1)) + 0.125
                * (reward(i - 1, j) + 0.9 * value(i - 1, j)) + 0.125
                * (reward(i + 1, j) + 0.9 * value(i + 1, j));

        return stateValue;
    }

    public static void main(String[] Args) {
        double initValue = 0.0;
        GridWorld gw = new GridWorld(initValue);
        double epsilon = 0.1;
        double[][] tempGrid = new double[9][9];
        double maximum = 0.0;
        do {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++) {
                    tempGrid[i][j] = gw.getStateExpectedValue(i, j);
                    if (Math.abs(tempGrid[i][j] - gw.value(i, j)) > maximum)
                        maximum = Math.abs(tempGrid[i][j] - gw.value(i, j));
                }
        } while (maximum > epsilon);

    }
}
