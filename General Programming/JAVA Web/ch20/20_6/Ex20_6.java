import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Ex20_6 extends Frame implements ActionListener {
  TalkRoom TalkRecord = new TalkRoom();
  TextField TalkInput = new TextField();
  
  public Ex20_6() {
     super("myFrame");
     try{
         add(TalkRecord, BorderLayout.CENTER);
         add(TalkInput, BorderLayout.SOUTH);
         TalkInput.addActionListener(this);

         setSize(500, 350);
         setVisible(true);

         addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
              System.exit(0);
            }
         });
      }
      catch (Exception e) {
         System.out.println(e.getMessage());
      }
  }

   public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == TalkInput) {
            try {
                TalkRecord.PrintTalk(TalkInput.getText(), Color.red);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            TalkInput.setText(null);
        } 
    }


  public static void main(String args[]) {
      Ex20_6 ServerStart=new Ex20_6();
  }
}

class TalkRoom extends Component {
    Vector PastLine = new Vector();
    Vector NewLine = new Vector();

    synchronized void PrintTalk(String s, Color c) {
        NewLine.addElement(new SaveLine(s, c));
        repaint();
    }

    public void paint(Graphics g) {
        synchronized (this) {
            while (NewLine.size() > 0) {
                PastLine.addElement(NewLine.elementAt(0));
                NewLine.removeElementAt(0);
            }
            while (PastLine.size() > 40) {
                PastLine.removeElementAt(0);
            }
        }
        FontMetrics fontM = g.getFontMetrics();
        int margin = fontM.getHeight()/2;
        int w = getSize().width;
        int y = getSize().height-fontM.getHeight()-margin;

        for (int i=PastLine.size()-1; i>=0; i--) {
            SaveLine ShowLine = (SaveLine)PastLine.elementAt(i);
            g.setColor(ShowLine.clr);
            g.setFont(new Font(ShowLine.str,Font.BOLD,12)); 
            g.drawString(ShowLine.str, margin, y+fontM.getAscent());
            y -= fontM.getHeight();
        }
    }
}

class SaveLine {
    String str;
    Color clr;
    SaveLine(String str, Color clr) {
        this.str = str;
        this.clr = clr;
    }
}
