
class Ex03_4_2 {
    public static void main (String[] argv) {
       byte bt;
       short sh;
       int in;
       long lg;
       float ft;
       double db;

       db = 97.0;
       ft = (float)db;
       System.out.println("float<--double¡G"+ft+"<--"+db);
       lg = (long)ft;
       System.out.println("long<--float¡G"+lg+"<--"+ft);
       in = (int)lg;
       System.out.println("int<--long¡G"+in+"<--"+lg);
       sh = (short)in;
       System.out.println("short<--int¡G"+sh+"<--"+in);
       bt = (byte)sh;
       System.out.println("bt<--short¡G"+bt+"<--"+sh);
     }
 }
       