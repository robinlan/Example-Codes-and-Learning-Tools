import java.io.*;

class NumberIssue { 
   int iniNumber, numberInterval;

   public NumberIssue(int iniNumber, int numberInterval) {
     this.iniNumber = iniNumber;
     this.numberInterval = numberInterval;
   }

   public synchronized int getNumber()
   //public int getNumber()
     {
      int num= iniNumber; 
      try {
           Thread.sleep(1000);
      } catch (InterruptedException e) {};

      iniNumber= iniNumber + numberInterval;
      return(num);     
     }
}

class Passanger extends Thread {
   int number;
   NumberIssue NI;

   public Passanger(NumberIssue NI) {
     this.NI = NI;
   } 
 
   public void run() {
     number =  NI.getNumber();
   }
}

public class Ex09_7_1 {
   public static void main(String[] args) {
     NumberIssue NI; 
     Passanger p1, p2, p3, p4;
 
     NI = new NumberIssue(1,2);

     p1 = new Passanger(NI);  p1.start();
     p2 = new Passanger(NI);  p2.start();
     p3 = new Passanger(NI);  p3.start();
     p4 = new Passanger(NI);  p4.start();

     try {
         p1.join(); p2.join(); p3.join(); p4.join();
     } catch (InterruptedException e) {};

    System.out.println("Passanger 1: Number: " + p1.number);
    System.out.println("Passanger 2: Number: " + p2.number);
    System.out.println("Passanger 3: Number: " + p3.number);
    System.out.println("Passanger 4: Number: " + p4.number);

   }
}