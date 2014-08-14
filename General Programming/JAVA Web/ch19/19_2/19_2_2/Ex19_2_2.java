import java.awt.*;

class Ex19_2_2 {
   public Ex19_2_2() { 
      Frame frame = new Frame("myFrame");
      frame.setCursor(Frame.HAND_CURSOR);
      frame.setSize(300, 350);
      frame.setVisible(true); 
   
      System.out.println("frame.getCursorType() : " + frame.getCursorType());
      System.out.println("frame.getTitle() : " + frame.getTitle());
      System.out.println("frame.isResizable() : " + frame.isResizable());
   }

    public static void main(String[] args) {
         Ex19_2_2 teststart = new Ex19_2_2();

    }
}
