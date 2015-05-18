import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class PredatorPrey {
    public static final int PERIOD = 50;
    public static final int START_X = 100;
    public static final int START_Y = 100;

    private double a,b,c,d,e,f,g,h;
    public PredatorPrey(double[] params) {
        a = params[0];
        b = params[1];
        c = params[2];
        d = params[3];
        e = params[4];
        f = params[5];
        if(params.length > 6) {
            g = params[6];
            h = params[7];
        }
    }

    public double evaluateX(double currentX, double currentY) {
        return currentX + a*currentX + b*currentY + e*Math.pow(currentX, 2);
    }

    public double evaluateY(double currentY, double currentX) {
        return currentY + c*currentX + d*currentY + f*Math.pow(currentY, 2);
    }

    public double evaluateXExt(double currentX, double currentY) {
        return currentX + a*currentX + b*currentY + e*Math.pow(currentX, 2) + g*currentX*currentY;
    }

    public double evaluateYExt(double currentY, double currentX) {
        return currentY + c*currentX + d*currentY + f*Math.pow(currentY, 2) + h*currentX*currentY;
    }

    public static void main(String[] args) {
        //a, b, c, d, e, f, g, h
        double[] params = new double[6];
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a");
        params[0] = in.nextDouble();
        System.out.println("Enter b");
        params[1] = in.nextDouble();
        System.out.println("Enter c");
        params[2] = in.nextDouble();
        System.out.println("Enter d");
        params[3] = in.nextDouble();
        System.out.println("Enter e");
        params[4] = in.nextDouble();
        System.out.println("Enter f");
        params[5] = in.nextDouble();

        PredatorPrey pp = new PredatorPrey(params);
        double currentX = START_X, currentY = START_Y, tempX;
        for(int i=0; i<PERIOD; i++) {
            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("quantities.dat", true)))) {
                out.println(i + " " + currentY + " " + currentX);
            }catch (IOException e) {
                e.printStackTrace();
            }
            tempX = currentX;
            currentX = pp.evaluateX(currentX, currentY);
            currentY = pp.evaluateY(currentY, tempX);
        }

        //a, b, c, d, e, f, g, h
        double[] paramsExt = new double[8];
        System.out.println("Enter a");
        paramsExt[0] = in.nextDouble();
        System.out.println("Enter b");
        paramsExt[1] = in.nextDouble();
        System.out.println("Enter c");
        paramsExt[2] = in.nextDouble();
        System.out.println("Enter d");
        paramsExt[3] = in.nextDouble();
        System.out.println("Enter e");
        paramsExt[4] = in.nextDouble();
        System.out.println("Enter f");
        paramsExt[5] = in.nextDouble();
        System.out.println("Enter g");
        paramsExt[6] = in.nextDouble();
        System.out.println("Enter h");
        paramsExt[7] = in.nextDouble();

        in.close();

        PredatorPrey ppExt = new PredatorPrey(paramsExt);
        currentX = START_X;
        currentY = START_Y;
        for(int i=0; i<PERIOD; i++) {
            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("quantities-extended.dat", true)))) {
                out.println(i + " " + currentY + " " + currentX);
            }catch (IOException e) {
                e.printStackTrace();
            }
            tempX = currentX;
            currentX = ppExt.evaluateXExt(currentX, currentY);
            currentY = ppExt.evaluateYExt(currentY, tempX);
        }
    }
}
