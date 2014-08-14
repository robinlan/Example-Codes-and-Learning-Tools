public class Ex08_2_3 {
     static String msg;
     static int num;
     
     Ex08_2_3() {
        System.out.println(msg);
        System.out.println(num);
     }

     public static void main(String[] args) {
        if(args.length < 2) {
           System.out.println("Usage: java Ex08_2_3 [msg] [num]");
           System.exit(1);
        }
        msg = args[0];
        num = Integer.parseInt(args[1]);
        Ex08_2_3 a = new Ex08_2_3();
     }
}
