class addThread1 extends Thread {
  public void run() {
    for(int i = 1; i <= 5; i++){
      System.out.println("執行緒 1    累加 2 → " + 2 * i);
      try {
        Thread.sleep((long)(1000 * Math.random()));
      }
      catch(InterruptedException e) {}
    }
  }
}

class addThread2 extends Thread {
  public void run() {
    for(int i = 1; i <= 5; i++){
      System.out.println("執行緒 2    累加 5 → " + 5 * i);
      try {
        Thread.sleep((long)(1000 * Math.random()));
      }
      catch(InterruptedException e) {}
    }
  }
}

public class J8_4_1 {
  public static void main (String[] args) {
    addThread1 thread1 = new addThread1();
    addThread2 thread2 = new addThread2();
    thread1.start();
    thread2.start();
  }
}