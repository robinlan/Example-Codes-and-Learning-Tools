package com.bn.wlxq;
import java.io.*;
import java.net.*;

public class Server
{
	
	static int count=0;//玩家数量
	static ServerAgent player1;//第一个玩家
	static ServerAgent player2;//第二个玩家
	static ServerAgent currPlayer;//当前玩家
	
	
	public static void main(String args[]) throws Exception
	{
		ServerSocket ss=new ServerSocket(9999);//对端口进行监听
		System.out.println("Listening on 9999...");
		while(true)
		{
			Socket sc=ss.accept();
			DataInputStream din=new DataInputStream(sc.getInputStream());//获取输入输出流
			DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
			
			if(count==0)
			{//进来的为黑方
				dout.writeUTF("<#ACCEPT#>1");//如果是第一个人进来,那么发对方是第一个人消息,
				System.out.println("<#ACCEPT#>1");
				player1=new ServerAgent(sc,din,dout);//一个客服端的代理线程,
				player1.start();
				count++;
			}
			else if(count==1)//如果是第二个人进入,那么发它是第二个人并且为北方
			{//进来的为白方
				dout.writeUTF("<#ACCEPT#>2");
				player2=new ServerAgent(sc,din,dout);
				player2.start();
				count++;
				
				//两个人已经有两个玩家,开始发加载界面消息
				player1.dout.writeUTF("<#START#>");
				player2.dout.writeUTF("<#START#>");
				
				//向黑方发送下棋权
				player1.dout.writeUTF("<#PERMISIION#>");
				currPlayer=player1;
				
			}
			else if(count==2)//如果是第三个人进来了
			{
				dout.writeUTF("<#FULL#>");
				System.out.println("<#FULL#>");
				dout.close();
				din.close();
				sc.close();
			}
		}
	}
}