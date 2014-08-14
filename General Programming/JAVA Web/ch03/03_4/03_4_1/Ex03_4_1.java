
class Ex03_4_1 {
    public static void main (String[] argv) {
       byte bt;
       short sh;
       int in;
       long lg;
       float ft;
       double db;

       bt = 97;
       sh = bt;
       System.out.println("short<--byte¡G"+sh+"<--"+bt);
       in = sh;
       System.out.println("int<--short¡G"+in+"<--"+sh);
       lg = in;
       System.out.println("long<--int¡G"+lg+"<--"+in);
       ft = lg;
       System.out.println("float<--long¡G"+ft+"<--"+lg);
       db = ft;
       System.out.println("double<--float¡G"+db+"<--"+ft);
     }
 }
       