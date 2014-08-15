package com.bn.wlqp;
import java.io.*;
import java.net.*;
import static com.bn.wlqp.Server.*;

public class ServerAgent extends Thread
{
	Socket sc; //网络套接字
	DataInputStream din;
	DataOutputStream dout;
	boolean flag=true;
	int count=17;
	
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
				if(msg.startsWith("<#PLAY#>"))
				{  //客户端发牌并且告知服务器 并且服务器判断下一个出牌的玩家
					String cards=msg.substring(8);
					ServerAgent next=null;
					String mTemp="<#CURR#>";
					if(currPlayer==player1)
					{
						mTemp=mTemp+"1";
						next=player2;
					}
					else if(currPlayer==player2)
					{
						mTemp=mTemp+"2";
						next=player3;
					}
					else if(currPlayer==player3)
					{
						mTemp=mTemp+"3";
						next=player1;
					}
					
					player1.dout.writeUTF(mTemp);
					player2.dout.writeUTF(mTemp);
					player3.dout.writeUTF(mTemp);
					
					mTemp="<#CARDS#>"+cards;//上一个玩家发的牌的信息
					
					player1.dout.writeUTF(mTemp);
					player2.dout.writeUTF(mTemp);
					player3.dout.writeUTF(mTemp);
					
					next.dout.writeUTF("<#YOU#>");//下一个获得牌权
					currPlayer=next;
				}
				else if(msg.startsWith("<#COUNT#>"))
				{//转发<#COUNT#>  发送要出的牌的牌号和当前玩家的标志位
					player1.dout.writeUTF(msg);
					player2.dout.writeUTF(msg);
					player3.dout.writeUTF(msg);
				}
				else if(msg.startsWith("<#I_WIN#>"))
				{//收到<#I_WIN#>(赢的)消息  并发送<#FINISH#>消息
					int currNumTemp=-1;
					if(this==player1)
					{
						currNumTemp=1;
					}
					else if(this==player2)
					{
						currNumTemp=2;
					}
					else if(this==player3)
					{
						currNumTemp=3;
					}
					
					Server.count=0;//服务器记录连接的人数清零
					
					player1.dout.writeUTF("<#FINISH#>"+currNumTemp);
					player2.dout.writeUTF("<#FINISH#>"+currNumTemp);
					player3.dout.writeUTF("<#FINISH#>"+currNumTemp);
					
					//一局游戏结束 关闭程序虚拟服务器端线程 并且关闭输入输出流和网络套接字
					player1.flag=false;
					player1.dout.close();
					player1.din.close();
					player1.sc.close();
					
					player2.flag=false;
					player2.dout.close();
					player2.din.close();
					player2.sc.close();
					
					player3.flag=false;
					player3.dout.close();
					player3.din.close();
					player3.sc.close();
				}
				else if(msg.startsWith("<#NO_PLAY#>"))
				{//客户端点击放弃按钮之后 服务器端判断下一个玩家是谁
					ServerAgent next=null;
					if(currPlayer==player1)
					{
						next=player2;
					}
					else if(currPlayer==player2)
					{
						next=player3;
					}
					else if(currPlayer==player3)
					{
						next=player1;
					}
					
					next.dout.writeUTF("<#YOU#>");
					currPlayer=next;
				}
				else if(msg.startsWith("<#EXIT#>"))
				{//收到客户端退出消息 并进行相关的设置
					Server.count=0;
						
					player1.dout.writeUTF("<#EXIT#>");
					player2.dout.writeUTF("<#EXIT#>");
					player3.dout.writeUTF("<#EXIT#>");
					
					//有客户端退出 关闭程序虚拟服务器端线程 并且关闭输入输出流和网络套接字
					player1.flag=false;
					player1.dout.close();
					player1.din.close();
					player1.sc.close();
					
					player2.flag=false;
					player2.dout.close();
					player2.din.close();
					player2.sc.close();
					
					player3.flag=false;
					player3.dout.close();
					player3.din.close();
					player3.sc.close();
				}
				else if(msg.startsWith("<#SCORE#>"))
				{
					scoresMsg[scoresIndex++]=msg;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}