package com.bn.wlxq;
import java.io.*;
import java.net.*;
import static com.bn.wlxq.Server.*;

public class ServerAgent extends Thread
{
	Socket sc;
	DataInputStream din;
	DataOutputStream dout;
	boolean flag=true;
	
	public ServerAgent(Socket sc,DataInputStream din,DataOutputStream dout)
	{		
		this.sc=sc;
		this.din=din;
		this.dout=dout;		
	}
	
	public void run()
	{
		while(flag)
		{
			try
			{
				String msg=din.readUTF();
				System.out.println(msg);
				
				if(msg.startsWith("<#MOVE#>"))
				{
					player1.dout.writeUTF(msg);
					player2.dout.writeUTF(msg);
					
					ServerAgent next=null;
					if(currPlayer==player1)
					{
						next=player2;
					}
					else
					{
						next=player1;
					}
					
					next.dout.writeUTF("<#PERMISIION#>");
					currPlayer=next;
				}
				else if(msg.startsWith("<#FINISH#>"))
				{
					player1.dout.writeUTF(msg);
					player2.dout.writeUTF(msg);
					
					player1.flag=false;
					player2.flag=false;
					
					player1.dout.close();
					player1.din.close();
					player1.sc.close();
					
					player2.dout.close();
					player2.din.close();
					player2.sc.close();
					
					Server.count=0;
					player1=null;
					player2=null;
					currPlayer=null;
				}
				else if(msg.startsWith("<#EXIT#>"))
				{
					player1.dout.writeUTF(msg);
					player2.dout.writeUTF(msg);
					
					player1.flag=false;
					player2.flag=false;
					
					player1.dout.close();
					player1.din.close();
					player1.sc.close();
					
					player2.dout.close();
					player2.din.close();
					player2.sc.close();
					
					Server.count=0;
					player1=null;
					player2=null;
					currPlayer=null;
					
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}