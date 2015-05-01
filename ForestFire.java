
public class ForestFire {
    public static int WIDTH = 101;
    public static int HEIGHT = 82;
    
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
        //TODO
        //set all cells to ashes
    }
    
    public void timeStep(){
        char[][] gridTemp = new char[WIDTH][HEIGHT];
        for(int i = 0; i < WIDTH; i++)
            for(int j = 0; j < HEIGHT; j++){
                
            }
        
    }
    
    public void statesToFile(){
        //TODO
    }
    
    public void printOut(){
        //TODO
    }
    
    public static void main(String[] args){
       //enter p, f, q
        double p = 0.9, q = 0.7, f = 0.5;
        //TODO open file
        //modify statesToFire arg list to take file descriptor
      
        ForestFire ff = new ForestFire(p, f, q);
        while(true){
            ff.printOut();
            ff.statesToFile();
            ff.timeStep();
            
            //TODO clear console(mb move to prinOut)
                
        }
        
    }
    

}
