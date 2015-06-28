import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Robot {
    public static int WORLD_SIZE = 50;
    public static ArrayList<String> occupied = new ArrayList<String>();

    private int orientation;
    private int currentX, currentY;
    private int[] sensorValues = new int[8];
    public Robot(int x, int y, int direction) {
        //position in world
        currentX = x;
        currentY = y;
        //from 0 to 7 - one of possible directions
        //starting from 0 to the north and moving CW
        orientation = direction;
    }

    //Braitenberg 3b
    public void makeMove() {
        sence();
        int test = orientation;
        boolean canMove = sensorValues[orientation]==0;
        //trying to find a way turning CW
        if(!canMove) {
            while(test<8 && sensorValues[test]==1)
                test++;
            if(test!=8)
                canMove = sensorValues[test]==0;
            if(canMove)
                orientation = test;
            else {
                //start from 0 orientation, or we will always just go back the way we came
                test = 0;
                while(test<orientation && sensorValues[test]==1)
                    test++;
                canMove = sensorValues[test] == 0;
                if(canMove)
                    orientation = test;
            }
        }

        if(canMove) {
            int[] nextPose = getNextPose();
            currentX = nextPose[0];
            currentY = nextPose[1];
        }
    }

    private void sence() {
        Arrays.fill(sensorValues, 0);
        int x, y;
        //1
        y = currentY+1;
        if(currentY+1>=WORLD_SIZE || occupied.contains(currentX + "," + y))
            sensorValues[0] = 1;
        //2
        x = currentX+1;
        y = currentY+1;
        if(currentX+1>=WORLD_SIZE || currentY+1>=WORLD_SIZE || occupied.contains(x + "," + y))
            sensorValues[1] = 1;
        //3
        x = currentX+1;
        if(currentX+1>=WORLD_SIZE || occupied.contains(x + "," + currentY))
            sensorValues[2] = 1;
        //4
        x = currentX+1;
        y = currentY-1;
        if(currentX+1>=WORLD_SIZE || currentY-1<0 || occupied.contains(x + "," + y))
            sensorValues[3] = 1;
        //5
        y = currentY-1;
        if(currentY-1<0 || occupied.contains(currentX + "," + y))
            sensorValues[4] = 1;
        //6
        x = currentX-1;
        y = currentY-1;
        if(currentX-1<0 || currentY-1<0 || occupied.contains(x + "," + y))
            sensorValues[5] = 1;
        //7
        x = currentX-1;
        if(currentX-1<0 || occupied.contains(x + "," + currentY))
            sensorValues[6] = 1;
        //8
        x = currentX-1;
        y = currentY+1;
        if(currentX-1<0 || currentY+1>=WORLD_SIZE || occupied.contains(x + "," + y))
            sensorValues[7] = 1;
    }

    private int[] getNextPose() {
        int[] result = {currentX, currentY};
        switch(orientation) {
        case 0: {
            result[1] += 1;
            break;
        }
        case 1: {
            result[0] += 1;
            result[1] += 1;
            break;
        }
        case 2: {
            result[0] += 1;
            break;
        }
        case 3: {
            result[0] += 1;
            result[1] -= 1;
            break;
        }
        case 4: {
            result[1] -= 1;
            break;
        }
        case 5: {
            result[0] -= 1;
            result[1] -= 1;
            break;
        }
        case 6: {
            result[0] -= 1;
            break;
        }
        case 7: {
            result[0] -= 1;
            result[1] += 1;
            break;
        }
        }
        return result;
    }

    public String position() {
        return currentX + " " + currentY + " " + orientation;
    }

    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("SS15_4201_PA-F.grid"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.nextLine();
        int x, y;
        while(scanner.hasNextLine()) {
            x = scanner.nextInt();
            y = scanner.nextInt();
            occupied.add(x + "," + y);
        }

        File output = new File("PA-F_robot.path");
        if (output.exists()) output.delete();

        //4 for south direction
        Robot bot = new Robot(48, 17, 4);
        for(int i=0; i<201; i++) {
            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("PA-F_robot.path", true)))) {
                out.println(bot.position());
            }catch (IOException e) {
                e.printStackTrace();
            }

            bot.makeMove();
        }
    }
}
