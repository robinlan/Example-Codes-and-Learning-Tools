public class Ex08_2_2 {
     static int num;
     
     Ex08_2_2() {
        num = num+1;
        System.out.println(num);
     }

     public static void main(String[] args) {
        num = Integer.parseInt(args[0]);
        Ex08_2_2 a = new Ex08_2_2();
     }
}
