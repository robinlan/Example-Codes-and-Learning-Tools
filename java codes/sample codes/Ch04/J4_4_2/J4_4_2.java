public class J4_4_2 {
  public static void main (String[] args){
    String[][] data;
    data = new String[][] {{"王正義","校長","義工"},{"黃哲夫","家管"},
                           {"宋麗美","工友","律師","市長"}}; 
    int i,j;
    for(i = 0; i < data.length; i++){
      System.out.print(data[i][0] + " : ");
      for(j = 1; j < data[i].length; j++){
        if(j == data[i].length - 1)
          System.out.print(data[i][j]);
        else
          System.out.print(data[i][j] + " , ");
      }
      System.out.println();
    }
  }
}