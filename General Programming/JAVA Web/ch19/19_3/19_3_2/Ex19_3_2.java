import java.awt.*;

class Ex19_3_2 extends Frame {
   public Ex19_3_2() { 
      super("myFrame");
      setSize(300, 350);
      setVisible(true);  

      TextField tf = new TextField("Initial word");
      add(tf); 
      tf.setText("The new setText");
      System.out.println("tf.getText() = " +  tf.getText()); 
   }

    public static void main(String[] args) {
         Ex19_3_2 teststart = new Ex19_3_2();
    }
}
