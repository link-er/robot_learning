import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class ForestFire {
    public static int WIDTH = 101;
    public static int HEIGHT = 82;
    public static final char ASH = '_';
    public static final char FIRE = '#';
    public static final char TREE = 'T';

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
        char[] currentNeighb;
        Random rand = new Random();
        double generated;
        boolean set;
        for(int i = 0; i < WIDTH; i++)
            for(int j = 0; j < HEIGHT; j++){
                set = false;
                switch(grid[i][j]) {
                case ASH: {
                    //check neighbors(i, j) - if any TREE
                    //check by probability of induced growth will be a tree here
                    //if no TREE in n\b - check by probability of spontaneous growth
                    //if a tree will be here
                    currentNeighb = neighbors(i, j);
                    for(char neighbor : currentNeighb) {
                        if(neighbor==TREE) {
                            generated = rand.nextDouble();
                            if(generated < inducedGrowth) {
                                gridTemp[i][j] = TREE;
                                set = true;
                                break;
                            }
                        }
                    }
                    if(!set) {
                        generated = rand.nextDouble();
                        if(generated < spontGrowth) {
                            gridTemp[i][j] = TREE;
                            set = true;
                        }
                    }
                    if(!set) gridTemp[i][j] = ASH;
                    break;
                }
                case FIRE: {
                    gridTemp[i][j] = ASH;
                    break;
                }
                case TREE: {
                    //check neighbors(i, j) - if any FIRE
                    //set on fire
                    //if no FIRE in n\b - check by probability of spontaneous fire
                    //if fire will be here
                    currentNeighb = neighbors(i, j);
                    for(char neighbor : currentNeighb) {
                        if(neighbor==FIRE) {
                            gridTemp[i][j] = FIRE;
                            set = true;
                            break;
                        }
                    }
                    if(!set) {
                        generated = rand.nextDouble();
                        if(generated < spontFire) {
                            gridTemp[i][j] = FIRE;
                            set = true;
                        }
                    }
                    if(!set) gridTemp[i][j] = TREE;
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

        int loopedIndex = (j-1)%HEIGHT;
        if (loopedIndex < 0) loopedIndex += HEIGHT;
        result[0] = grid[i][loopedIndex];

        loopedIndex = (i-1)%WIDTH;
        if (loopedIndex < 0) loopedIndex += WIDTH;
        result[1] = grid[loopedIndex][j];

        result[2] = grid[i][(j+1)%HEIGHT];
        result[3] = grid[(i+1)%WIDTH][j];
        return result;
    }

    public void statesToFile() {
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("states.txt", true)))) {
            out.println(countAshes + " " + countTrees + " " + countFire);
        }catch (IOException e) {
            e.printStackTrace();
        }
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
