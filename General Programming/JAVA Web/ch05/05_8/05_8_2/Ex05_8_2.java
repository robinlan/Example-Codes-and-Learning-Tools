class myClass1 {
      public void f() {System.out.println("In f of myClass1");
      }
}

class myClass2 extends myClass1 {
      public void g() {System.out.println("In g of myClass2");
	}
}

class Ex05_8_2 {
      public static void main(String[] args) {
             myClass1 a = new myClass2();
             a.f();
      }
}
