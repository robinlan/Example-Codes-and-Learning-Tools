import java.awt.*;

class Ex19_3_1 extends Frame {
   public Ex19_3_1() { 
      super("myFrame");
      setSize(300, 350);
      setVisible(true);  

      TextField tf = new TextField("Initial word");
      add(tf);   
   }

    public static void main(String[] args) {
         Ex19_3_1 teststart = new Ex19_3_1();

    }
}
