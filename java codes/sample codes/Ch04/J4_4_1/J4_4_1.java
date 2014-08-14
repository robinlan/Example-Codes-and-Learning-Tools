public class J4_4_1 {
  public static void main (String[] args){
    double[][] data;
    data = new double[2][];
    data[0] = new double[1];
    data[1] = new double[3];
    data[0][0] = 0.0;
    data[1][0] = 1.0; data[1][1] = 1.1; data[1][2] = 1.2;
    int i, j;
    for(i = 0; i < 2; i++){
      for(j = 0; j < data[i].length; j++) {
        if(j == data[i].length-1)
          System.out.print(data[i][j]);
        else
          System.out.print(data[i][j] + ", ");
      }
      System.out.println();
    }
  }
}