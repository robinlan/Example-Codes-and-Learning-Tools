class myRunnable implements Runnable {
  public void run() {
    System.out.println("Message by myRunnable");
  }
}

public class Ex17_2 {
  public Ex17_2() {
     myRunnable testR = new myRunnable();
     Thread testTh = new Thread(testR);
     testTh.start();
  }

  public static void main(String[] args) throws Exception {
      Ex17_2 ServerStart=new Ex17_2();
  }
}

