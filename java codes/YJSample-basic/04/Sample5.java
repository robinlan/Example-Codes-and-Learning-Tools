class Sample5
{
   public static void main(String[] args)
   {
      int a = 0;
      int b = 0;

      b = a++;

      System.out.println("因為是在指定值之後才遞增，所以b的值為" + b + "。");
   }
}
