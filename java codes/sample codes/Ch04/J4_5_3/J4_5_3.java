import java.io.*;
public class J4_5_3 {
  public static void main (String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    int[] account = {18,14,12,16,10};
    String[] name = {"王五","張三","陳二","李四","鄭一"}; 
    int i, j, account_t, tot;
    String name_t;
        
    tot = account.length;
    for(i = tot-2; i >= 0; i--) 
      for(j = 0; j <= i; j++)
        if(account[j] > account[j + 1]) {
          account_t = account[j + 1];
          account[j + 1] = account[j];
          account[j] = account_t;
          name_t = name[j + 1];
          name[j + 1] = name[j];
          name[j] = name_t;
        }   
    for(i=0; i < tot; i++) {
      System.out.println(" " + account[i] + "\t" + name[i]);
    }

    System.out.println("\n 二 分 搜 尋 法" );
    System.out.print(" 輸 入 編 號 : " );
    int search_account = Integer.parseInt(keyin.readLine());

    int low,high,mid_num,num;       
    low = 0; high = tot-1;
    mid_num =(low + high) / 2;
    while(true){
      System.out.println("low=" + low + "   " + "high=" + high 
                         + "   " + "mid_num=" + mid_num);
      if(account[mid_num] == search_account) {
        num = mid_num;
        break;
      }
      if(low == high || mid_num < low || mid_num > high) {
        num = -1;
        break;
      }
      if(account[mid_num] > search_account)
        high = mid_num - 1;
      else
        low = mid_num + 1;
        mid_num = (low + high) / 2;
    }
    if (num == -1)
      System.out.println("\n 查 無 此 編 號 " );
    else {
      System.out.println("\n 編號\t姓名");
      System.out.println(" " + account[num] + "\t" + name[num]);
    }
  }
}