public class Ex08_2_1 {
     static String msg;
     
     Ex08_2_1() {
        System.out.println(msg);
     }

     public static void main(String[] args) {
        msg = args[0];
        Ex08_2_1 a = new Ex08_2_1();
     }
}
