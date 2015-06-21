public class Point {
	private double[] point;
	private int value;
	
	public Point(String data){
		
		String[] dataArray = data.split(" ");
		for(int i=0; i<dataArray.length; i++)
		System.out.print(dataArray[i]+"    ");
		System.out.println();
		int size = dataArray.length;
		point = new double[size-2];
		for(int i=1;i<size-1;i++){
			
				
			point[i-1]=Double.parseDouble(dataArray[i]);
			//
			}
		
		value = Integer.parseInt(dataArray[0]);
		System.out.println("point!!!!");
	}
	
	public Point(Point otherPoint){
		point = new double[otherPoint.point.length];
		System.arraycopy(otherPoint, 0, point, 0, otherPoint.point.length);
		/*for(int i=0; i<otherPoint.point.length;i++)
			point[i] = otherPoint.point[i];*/
		value = otherPoint.value;
	}
	public double[] getPoint(){
		return point;
	}
	
	public int getValue(){
		return value;
	}

}
