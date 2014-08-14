class BBKMath {
  static void GenRnd(int[] x, int min, int max, int rnd_no) {  
    int tot_no, rem_no, t_no;
    int i, j;
    tot_no = max - min + 1;
    int[] t = new int[tot_no];
    for(i = 0; i < tot_no; i++) {
      t[i] = min + i;
    }
    rem_no = tot_no;
    for( i = 0; i < rnd_no; i++) {
      t_no = (int)Math.floor(Math.random() * rem_no );
      x[i] = t[t_no];
      for( j = t_no; j < (rem_no - 1); j++) {
        t[j] = t[j+1];
      }
      rem_no--;
    }
  }
}

public class J6_8_2 {
  public static void main(String[] args)  {
    int min = 1, max = 49 ,rnd_no = 7; 
    int tot_no = max - min + 1;
    int[] data = new int[tot_no];
          
    BBKMath OData = new BBKMath(); 
    OData.GenRnd(data, min, max, rnd_no);
    System.out.print("大樂透中獎號碼 : ");
    for(int i = 1; i <= 6 ; i++) {
      System.out.print(data[i]);
      if(i <= 5)   
        System.out.print(" , ");
      else
        System.out.println("    特別號：" + data[0]);                   
    }     
  }  
}
