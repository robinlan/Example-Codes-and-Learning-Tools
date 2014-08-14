import java.io.*;

class JOB extends Thread {
   int sum;
   boolean running = true;

   JOB (ThreadGroup grp, String threadName, int initValue) {
      super(grp, threadName);
      this.sum = initValue;
    }

    public void run()  {
       while(running) 
          sum = sum + 1;
    }
}

public class Ex09_8 {
   public static void main(String[] args) {
      ThreadGroup grp1 = new ThreadGroup("grp1");
      ThreadGroup grp2 = new ThreadGroup("grp2");

      JOB a1 = new JOB(grp1, "a1", 0);
      JOB a2 = new JOB(grp1, "a2", 0);
      JOB a3 = new JOB(grp2, "a3", 0);
      JOB a4 = new JOB(grp2, "a4", 0);

      a1.start();
      a2.start();
      a3.start();
      a4.start();

      try {Thread.sleep(1000);} 
          catch(InterruptedException e){}
   
      grp1.stop();
      grp2.stop();
    
      try {
         a1.join();
         a2.join();
         a3.join();
         a4.join();
       } catch (InterruptedException e) {};

      System.out.println("grp1_a1: sum = "+a1.sum);
      System.out.println("grp1_a2: sum = "+a2.sum); 
      System.out.println("grp2_a3: sum = "+a3.sum);
      System.out.println("grp2_a4: sum = "+a4.sum); 
   }
 }

