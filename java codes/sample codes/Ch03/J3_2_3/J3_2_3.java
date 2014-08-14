public class J3_2_3 {
  public static void main (String[] args){
    int eng = 80, computer = 91;
   	double avg;
   	avg = (double)(eng + computer) / 2;
    System.out.println("平均成績 = " + avg);
    System.out.println("電腦分數 = " + computer);
    if (avg >= 60) {
      if (computer >= 90) {
        System.out.println("恭喜！你已達錄取標準，且可保送資訊系");
      } else {
        System.out.println("恭喜！你已達錄取標準"); 
      }
    } else { 
      System.out.println("抱歉，你未達錄取標準！");
    }
  }
}