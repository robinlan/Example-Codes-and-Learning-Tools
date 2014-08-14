class Sample3
{
   public static void main(String[] args)
   {
      try{
         int test[];
         test = new int[5];

         System.out.println("將值指定到test[10]。");

         test[10] = 80;
         System.out.println("將80指定到test[10]。");

      }
      catch(ArrayIndexOutOfBoundsException e){

          System.out.println("超過陣列的範圍了。");

      }
      finally{

          System.out.println("最後一定會執行這個處理。");
      }
      System.out.println("順利地執行完畢了。");
   }
}