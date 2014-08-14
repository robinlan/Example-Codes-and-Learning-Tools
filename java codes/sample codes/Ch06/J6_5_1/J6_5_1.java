import java.io.*;
public class J6_5_1 {
  public static void main(String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    System.out.print("請輸入 CD 或 DVD ？ ");
    String st = keyin.readLine();
    System.out.println();      
        
    CCD p1 = new CCD();
    p1.Play(st);
       
    CDVD p2 = new CDVD();
    p2.Play(st);
  }
}

// 定義IPlayer介面
interface IPlayer {
  String d1 = "CD";
  String d2 = "DVD";
  void Play(String disk);
}

// 定義CCD類別實作IPlayer介面
class CCD implements IPlayer {
  public void Play(String disk) {
    if (disk.equals(d1)) 
      System.out.println ("現在播放的是『音樂』\n");
  }
}

// 定義CDVD類別實作IPlayer介面
class CDVD implements IPlayer {
  public void Play(String disk) {
    if (disk.equals(d2)) 
      System.out.println ("現在播放的是『電影』\n");
  }
}
