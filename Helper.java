import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Helper {
    public static List<Point> data;

    public static void readData(String filename) throws FileNotFoundException {
        data = new ArrayList<Point>();
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        //pass comments
        sc.nextLine();
        sc.nextLine();
        //let up to 150 points
        while (sc.hasNextLine() && data.size() < 150) {
            data.add(new Point(sc.nextLine()));
        }
        sc.close();
    }
    
    public static int[] permutation(int n) {
        int index1, index2;
        int[] str = new int[n];
        for (int i = 0; i < n; i++)
            str[i] = i + 1;
        int rand = (int) (Math.random() * (4*n/5));
        for (int i = 0; i < rand; i++) {
            index1 = (int) (Math.random() * n);
            index2 = (int) (Math.random() * n);
            swap(str, index1, index2);
        }
        return str;
    }

    public static void swap(int[] str, int i, int j) {
        int temp = str[i];
        str[i] = str[j];
        str[j] = temp;
    }
}
