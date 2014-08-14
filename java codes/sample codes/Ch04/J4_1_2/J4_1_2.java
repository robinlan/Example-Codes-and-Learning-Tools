public class J4_1_2 {
  public static void main (String[] args){
    int[] data;
    data = new int[]{2,4,10,6,8};
    int i, len, max_num;
    len = data.length;
    for(i = 0; i < len; i++){
      System.out.print(data[i] + ", ");
    }
    System.out.println();
    max_num = data[0];
    for(i = 1; i < len; i++){
      if(data[i] > max_num) max_num = data[i];            
    }
    System.out.println("³Ì¤j¼Æ" +  " : " + max_num);
  }
}