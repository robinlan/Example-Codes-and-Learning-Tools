class Sample6
{
   public static void main(String[] args)
   {
      int[] test1;
      test1 = new int[3];
      System.out.println("宣告test1。");
      System.out.println("確保陣列元素。");

      test1[0] = 80;
      test1[1] = 60;
      test1[2] = 22;

      int[] test2;
      System.out.println("宣告test2。");

      test2 = test1;
      System.out.println("將test1指定給test2。");

      for(int i=0; i<3; i++){
         System.out.println("test1所指的第" + (i+1) + "個人的分數是" + test1[i] + "分。");
      }

      for(int i=0; i<3; i++){
         System.out.println("test2所指的第" + (i+1) + "個人的分數是" + test2[i] + "分。");
      }

      test1[2] = 100;
      System.out.println("變更test1所指的第3個人的分數。");

      for(int i=0; i< test1.length; i++){
         System.out.println("test1所指的第" + (i+1) + "個人的分數是" + test1[i] + "分。");
      }

      for(int i=0; i< test2.length; i++){
         System.out.println("test2所指的第" + (i+1) + "個人的分數是" + test2[i] + "分。");
      }
   }
}
