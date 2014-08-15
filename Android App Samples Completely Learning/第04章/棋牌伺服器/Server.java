package com.bn.wlqp;
import java.io.*;
import java.net.*;
public class Server
{
	static int count=0;//玩家数量
	static ServerAgent player1;
	static ServerAgent player2;
	static ServerAgent player3;
	
	static ServerAgent currPlayer;//当前玩家
	static String[] scoresMsg;
	static int scoresIndex;
	
	public static void main(String args[]) throws Exception
	{
		ServerSocket ss=new ServerSocket(9999);
		System.out.println("Listening on 9999...");
		while(true)
		{
			Socket sc=ss.accept();
			DataInputStream din=new DataInputStream(sc.getInputStream());
			DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
			
			if(count==0)
			{//进来的是第一个人
				scoresMsg=new String[3];
				scoresIndex=0;
			    System.out.println("<#ACCEPT#>1");
				dout.writeUTF("<#ACCEPT#>1");
				player1=new ServerAgent(sc,din,dout);
				player1.start();
				count++;
			}
			else if(count==1)
			{//进来的是第二个人
			    System.out.println("<#ACCEPT#>2");
				dout.writeUTF("<#ACCEPT#>2");
				player2=new ServerAgent(sc,din,dout);
				player2.start();
				count++;
			}
			else if(count==2)
			{//进来的是第三个人
			    System.out.println("<#ACCEPT#>3");
				dout.writeUTF("<#ACCEPT#>3");	
				player3=new ServerAgent(sc,din,dout);
				player3.start();			
				count++;
				
				String[] cards=FPUtil.newGame();
				
				//三家发牌，切屏到游戏界面
				player1.dout.writeUTF(cards[0]);
				player2.dout.writeUTF(cards[1]);
				player3.dout.writeUTF(cards[2]);
				
				//给玩家1牌权
				player1.dout.writeUTF("<#YOU#>");
				currPlayer=player1;
				
				//给三个玩家发得分
				while(scoresIndex<3)
				{
					try
					{
						Thread.sleep(100);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}					
				}
				for(String s:scoresMsg)
				{
					player1.dout.writeUTF(s);
					player2.dout.writeUTF(s);
					player3.dout.writeUTF(s);
				}
				
			}
			else if(count==3)
			{//客户端人数已满
			    System.out.println("<#FULL#>");
				dout.writeUTF("<#FULL#>");
				dout.close();
				din.close();
				sc.close();
			}
		}
	}
}