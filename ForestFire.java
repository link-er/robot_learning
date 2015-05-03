import java.util.Scanner;

public class ForestFire {
    public static int WIDTH = 101;
    public static int HEIGHT = 82;
    public static final char ASH = '_';
    public static final char FIRE = '#';
    public static final char TREE = '~';

    private char[][] grid;
    private double spontGrowth, spontFire, inducedGrowth;
    private int countAshes, countTrees, countFire;

    public ForestFire(double p, double f, double q){
        spontGrowth = p;
        spontFire = f;
        inducedGrowth = q;
        grid = new char[WIDTH][HEIGHT];
        setInitial();
    }

    private void setInitial(){
        countAshes = WIDTH*HEIGHT;
        countTrees = 0;
        countFire = 0;
        //set all cells to ashes
        for(int i = 0; i < WIDTH; i++)
            for(int j = 0; j < HEIGHT; j++)
                grid[i][j] = ASH;
    }

    public void timeStep(){
        char[][] gridTemp = new char[WIDTH][HEIGHT];
        for(int i = 0; i < WIDTH; i++)
            for(int j = 0; j < HEIGHT; j++){
                switch(grid[i][j]) {
                case ASH: {
                    //TODO check neighbors(i, j) - if any TREE
                    //check by probability of induced growth will be a tree here
                    //if no TREE in n\b - check by probability of spontaneous growth
                    //if a tree will be here
                    break;
                }
                case FIRE: {
                    gridTemp[i][j] = ASH;
                    break;
                }
                case TREE: {
                    //TODO check neighbors(i, j) - if any FIRE
                    //set on fire
                    //if no FIRE in n\b - check by probability of spontaneous fire
                    //if fire will be here
                    break;
                }
                default: break;
                }
            }

        countAshes = 0;
        countTrees = 0;
        countFire = 0;
        for(int i = 0; i < WIDTH; i++)
            for(int j = 0; j < HEIGHT; j++) {
                grid[i][j] = gridTemp[i][j];
                switch(grid[i][j]) {
                case ASH: {
                    countAshes++;
                    break;
                }
                case FIRE: {
                    countFire++;
                    break;
                }
                case TREE: {
                    countTrees++;
                    break;
                }
                default: break;
                }
            }
    }

    private char[] neighbors(int i, int j) {
        char[] result = new char[4];
        //TODO check if % returns needed result
        result[0] = grid[i][(j-1)%HEIGHT];
        result[0] = grid[(i-1)%WIDTH][j];
        result[0] = grid[i][(j+1)%HEIGHT];
        result[0] = grid[(i+1)%WIDTH][j];
        return result;
    }

    public void statesToFile(){
        //TODO
    }

    public void printOut(){
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++)
                System.out.print(grid[i][j] + " ");
            System.out.println();
        }
    }

    public static void main(String[] args){
        double p = 0.0, q = 0.0, f = 0.0;
      //reading parameters
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println("Enter probability of spontanuos growth:");
            p = in.nextDouble();
            if (p>=0.0 && p<=1.0){
                break;
            }
        }

        while(true){
            System.out.println("Enter probability of spontanuos fire:");
            f = in.nextDouble();
            if (f>=0.0 && f<=1.0){
                break;
            }
        }

        while(true){
            System.out.println("Enter probability of induced growth:");
            q = in.nextDouble();
            if (q>=0.0 && q<=1.0){
                break;
            }
        }

        in.close();
        //TODO open file
        //modify statesToFire arg list to take file descriptor

        ForestFire ff = new ForestFire(p, f, q);
        while(true){
            System.out.println("\n\n\n\n\n");
            ff.printOut();
            ff.statesToFile();
            ff.timeStep();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                //Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


}
