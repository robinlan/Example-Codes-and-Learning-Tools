import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

class Client21 extends Frame implements ActionListener, Runnable {
  TalkRoom TalkWork = new TalkRoom();
  TextField TalkInput = new TextField();
  Socket socket;
  static String iaddr;
  static int port;
  static String user;
  DataOutputStream  outstream;
  DataInputStream  instream;
  
  public Client21() {
     super("Client");
     try{
         socket=new Socket(InetAddress.getByName(iaddr),port);
         outstream = new DataOutputStream(socket.getOutputStream());
         instream = new DataInputStream(socket.getInputStream()); 

         add(TalkWork, BorderLayout.CENTER);
         add(TalkInput, BorderLayout.SOUTH);
         TalkInput.addActionListener(this);

         addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
              System.exit(0);
            }
         });

         setSize(500, 350);
         setVisible(true);
         new Thread(this).start();
      }
      catch (Exception e) {
            e.printStackTrace();
      }
  }

   public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == TalkInput) {
            try {
                outstream.writeUTF(user+"> "+TalkInput.getText());
                TalkWork.PrintTalk(TalkInput.getText(), Color.red);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            TalkInput.setText(null);
        } 
    }

   public void run() {
        try {
            while (true) {
                String NetTransferLine = instream.readUTF();
                TalkWork.PrintTalk(NetTransferLine, Color.black);
            }
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


  public static void main(String args[]) {
      if (args.length < 3){
         System.out.println("USAGE: java Client21 [iaddr] [port] [user]");	
         System.exit(1);
      }

      iaddr= args[0];
      port=Integer.parseInt(args[1]);
      user=args[2];
      Client21 ClientStart=new Client21();
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
