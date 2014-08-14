import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

class Client20_7 extends Frame implements ActionListener, Runnable {
  TalkRoom TalkRecord = new TalkRoom();
  TextField TalkInput = new TextField();
  Socket socket;
  static String iaddr;
  static int port;
  DataOutputStream  outstream;
  DataInputStream  instream;
  
  public Client20_7() {
     super("Client");
     try{
         socket=new Socket(InetAddress.getByName(iaddr),port);
         outstream = new DataOutputStream(socket.getOutputStream());
         instream = new DataInputStream(socket.getInputStream()); 

         add(TalkRecord, BorderLayout.CENTER);
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
                outstream.writeUTF("Client> "+TalkInput.getText());
                TalkRecord.PrintTalk(TalkInput.getText(), Color.red);
            } catch (Exception e) {
                TalkRecord.PrintTalk("Connection is Interrupted", Color.green);
            }
            TalkInput.setText(null);
        } 
    }

   public void run() {
        try {
            while (true) {
                String NetTransferLine = instream.readUTF();
                TalkRecord.PrintTalk(NetTransferLine, Color.black);
            }
        } 
        catch (Exception e) {
            TalkRecord.PrintTalk("Connection is Interrupted!", Color.green);
        }
    }


  public static void main(String args[]) {
      if (args.length < 2){
         System.out.println("USAGE: java Client20_7 [iaddr] [port]");	
         System.exit(1);
      }

      iaddr = args[0];
      port=Integer.parseInt(args[1]);
      Client20_7 ClientStart=new Client20_7();
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
