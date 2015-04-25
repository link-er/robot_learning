package ca;

public class CA {
    private int[] arr, arr1, bin;

    public CA() {
        arr = new int[84];
        arr1 = new int[84];
        bin = new int[8];
        arr[42] = 1;

    }
      public void printFirstOut(){
          printOut(arr);
      }
    
    public void printOut(int[] a){
        for(int i = 0;i<84;i++)
            if(a[i]==0)
                System.out.print("-");
            else
                System.out.print("*");
        System.out.println();
    }
    

    public int[] binary(int rule) {
        String temp = Integer.toBinaryString(rule);
        System.out.println(temp);
        String[] tmp = temp.split("");
        for (int i = 0; i < tmp.length; i++)
            bin[bin.length - tmp.length + i] = Integer.parseInt(tmp[i]);
        for(int i = 0;i<8;i++)
            System.out.print(bin[i]);
        System.out.println();
        for(int i=0;i<bin.length/2;i++){
            int btmp = bin[bin.length - i-1];
            bin[bin.length - i-1] = bin[i];
            bin[i]=btmp;
        }
        for(int i = 0;i<8;i++)
            System.out.print(bin[i]);
        System.out.println();
        return bin;
    }

    public int[] someFunction() {
        for (int i = 2; i < 82; i++) {
            int sum = 0;
            for (int j = 2; j >= 0; j--)
                sum += (int) (Math.pow(2, j)) * arr[i + 1 - j];
            arr1[i]=bin[sum];
            
        }   
       
       for(int i=0;i<84;i++)
           arr[i]=arr1[i];
       return arr1;
    }

    public static void main(String[] args) {
        CA ca = new CA();
        ca.binary(30);
        ca.printFirstOut();
        for (int i = 0; i < 10; i++)
            ca.printOut(ca.someFunction());
    }
}
