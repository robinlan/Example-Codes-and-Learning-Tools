import java.io.*;
public class J4_5_2 {
  public static void main (String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    int[] account = {18,14,12,16,10};
    String[] name = {"王五","張三","陳二","李四","鄭一"}; 
    int i, num, search_num;
    System.out.println("\n 線 性 搜 尋 法" );
    System.out.print(" 輸 入 編 號 : " );
    search_num = Integer.parseInt(keyin.readLine());
    num = -1;
    for(i = 0; i < account.length; i++) {
      if(account[i] == search_num) {
        num = i;  
        break;
      }
    }
    if (num == -1)
      System.out.println("\n 查 無 此 編 號 " );
    else {
      System.out.println("\n 編號\t姓名");
      System.out.println(" " + account[num] + "\t" + name[num]);
    }
  }
}