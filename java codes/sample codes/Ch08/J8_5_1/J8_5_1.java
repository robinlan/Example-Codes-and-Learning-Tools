class addThread implements Runnable {
  private String thread_n;
  private int num;
  addThread(String name, int n) {
    thread_n = name;
    num = n;
  } 
  public void run() {
    for(int i = 1; i <= 5; i++) {
      System.out.println(thread_n + "    ²Ö¥[ " + num +" ¡÷ "+ num * i);
      try {
        Thread.sleep((long)(1000 * Math.random()));
      }
      catch(InterruptedException e) {}
    }
  }
}

public class J8_5_1 {
  public static void main (String[] args) {
    addThread thread1 = new addThread("°õ¦æºü 1", 2);
    addThread thread2 = new addThread("°õ¦æºü 2", 5);
    Thread t1 = new Thread(thread1);
    Thread t2 = new Thread(thread2);
    t1.start();
    t2.start();
  }
}