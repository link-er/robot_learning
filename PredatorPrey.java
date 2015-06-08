import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class PredatorPrey {
    public static final int PERIOD = 100;
    public static final int START_X = 500;
    public static final int START_Y = 40;

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
        //a, b, c, d, e, f
        //x(i+1) = x(i)+a∗x(i)+b∗y(i)+e∗x(i)∗x(i)
        //y(i+1) = y(i)+c∗x(i)+d∗y(i)+f ∗y(i)∗y(i)
        double[] params = { 0.0, -2.0, 0.5, 0.5, -0.1, -0.1 };

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
        //x(i+1) = x(i)+a∗x(i)+b∗y(i)+e∗x(i)∗x(i)+g∗x(i)∗y(i)
        //y(i+1) = y(i)+c∗x(i)+d∗y(i)+f ∗y(i)∗y(i)+h∗x(i)∗y(i)
        double[] paramsExt = { 0.09, 0.0, 0.0, 0.0, 0.0, -0.03, -0.004, 0.0001 };

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
