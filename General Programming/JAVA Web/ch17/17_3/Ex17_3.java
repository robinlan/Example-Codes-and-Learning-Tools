class Client1 implements Runnable {
  public void run() {
    System.out.println("Message by Client1");
  }
}

class Client2 implements Runnable {
  public void run() {
    System.out.println("Message by Client2");
  }
}

class Client3 implements Runnable {
  public void run() {
    System.out.println("Message by Client3");
  }
}

public class Ex17_3 {
  public Ex17_3() {
     Client1 client1 = new Client1();
     Thread thread1 = new Thread(client1);
     thread1.start();

     Client2 client2 = new Client2();
     Thread thread2 = new Thread(client2);
     thread2.start();

     Client3 client3 = new Client3();
     Thread thread3 = new Thread(client3);
     thread3.start();

     Thread threadEx17_3 = new Thread();
     int act = threadEx17_3.activeCount();
     System.out.println("threadEx17_3.activeCount() = " + act);

     Thread curr = threadEx17_3.currentThread();
     System.out.println("threadEx17_3.currentThread() = " + curr.getName());

  }

  public static void main(String[] args) throws Exception {
      Ex17_3 ServerStart=new Ex17_3();
  }
}

