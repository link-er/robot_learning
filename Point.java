import java.util.Arrays;

public class Point {
    private double[] point;
    private int value;

    public Point(String data){
        String[] dataArray = data.split(" ");
        int size = dataArray.length;
        point = new double[size-2];
        for(int i=1;i<size-1;i++)
            point[i-1]=Double.parseDouble(dataArray[i]);

        value = Integer.parseInt(dataArray[0]);
    }

    public Point(Point otherPoint){
        point = new double[otherPoint.point.length];
        System.arraycopy(otherPoint, 0, point, 0, otherPoint.point.length);
        value = otherPoint.value;
    }
    public double[] getPoint(){
        return point;
    }

    public int getValue(){
        return value;
    }

    public String toString() {
        return value + ":" + Arrays.toString(point);
    }

}
