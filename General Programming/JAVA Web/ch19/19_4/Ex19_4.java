import java.awt.*;

class Ex19_4 extends Frame {
   public Ex19_4() { 
      super("myFrame");

      TextField tfE = new TextField("East Area");
      add(tfE, BorderLayout.EAST);

      TextField tfS = new TextField("South Area");
      add(tfS, BorderLayout.SOUTH);

      TextField tfW = new TextField("West Area");
      add(tfW, BorderLayout.WEST);

      TextField tfN = new TextField("North Area");
      add(tfN, BorderLayout.NORTH);

      TextField tfC = new TextField("Center Area");
      add(tfC, BorderLayout.CENTER);

      setSize(350, 300);
      setVisible(true);  
   }

    public static void main(String[] args) {
      Ex19_4 teststart = new Ex19_4();
    }
}
