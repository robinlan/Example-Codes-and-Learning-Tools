class Sample1
{
   public static void main(String[] args)
   {
       String str = "Hello";

       char ch1 = str.charAt(0);
       char ch2 = str.charAt(1);

       int len = str.length();

       System.out.println(str + "的第一個字元是" + ch1 + "。");
       System.out.println(str + "的第二個字元是" + ch2 + "。");
       System.out.println(str + "的長度是" + len + "。");
   }
}
