class A
{
   A()
   {
      System.out.println("硂琌⊿Τ把计篶Α");
   }
   A(int a)
   {
      this();
      System.out.println("硂琌把计篶Α");
   }
}
class SampleP4
{
   public static void main(String[] args)
   {
      A a1 = new A();
      A a2 = new A(10);
   }
}