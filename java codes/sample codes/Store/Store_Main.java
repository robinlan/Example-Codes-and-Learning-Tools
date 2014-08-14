import java.sql.*;
import java.util.Scanner;

public class Store_Main {
  public static void main(String[] args) {
    // 資料庫名稱
    String db_name="store";
	CStore_Operation DataManager = new CStore_Operation(db_name);
    boolean exit = false;
    Scanner reader = new Scanner(System.in); 
    while(!exit) {
      System.out.println("=========================================="); 
      System.out.print("1. 新增  2. 刪除 3.全部貨品 4. 結束  : "); 
      int choice = reader.nextInt(); 
      System.out.println("==========================================");                     
      switch(choice) {
        case 1:     
             // 輸入貨品資料
             System.out.println("1. 新增貨品 ");
		     System.out.print("輸入貨品編碼 : ");
             reader = new Scanner(System.in); 		 
		     String code= reader.nextLine();
		     System.out.print("輸入貨品單價 : ");		 
		     float price= reader.nextFloat();
		     System.out.print("輸入貨品數量 : ");	 
		     int num = reader.nextInt();
		     DataManager.InsertDB(code,price,num);
             break;
 		case 2: 
		     // 刪除貨品資料
             System.out.println("2. 刪除貨品 ");	
		     System.out.print("輸入貨品編碼 : ");
		     reader = new Scanner(System.in); 		 
		     String search_code2= reader.nextLine();    
             DataManager.DeleteDB(search_code2);
             break;
        case 3:
             // 輸出全部貨品資料
             System.out.println("3. 全部貨品資料 ");
             System.out.println("\n編碼\t單價\t數量\t金額");
             DataManager.Display();
             break;
        case 4:
             exit = true;
             break;
      }
    }
  }
}
