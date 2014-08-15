package com.bn.wlqp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientAgent extends Thread
{
	WLQPActivity father;
	Socket sc;
	DataInputStream din;
	DataOutputStream dout;
	boolean flag=true;
	int selfNum=0;//自己的玩家编号
	String lastCards;//上一次打的牌
	boolean perFlag;//出牌权标志
	int lastNum=-1;//上一次出牌的玩家编号
	int scores[]=new int[3];
	
	int shangjiaCount=17;//上家牌数
	int xiajiaCount=17;//下家牌数
	
	public ClientAgent(WLQPActivity father,Socket sc,DataInputStream din,DataOutputStream dout)
	{
		this.father=father;
		this.sc=sc;
		this.din=din;
		this.dout=dout;
	}
	@Override
	public void run()
	{
		while(flag)
		{
			try
			{
				final String msg=din.readUTF();
				System.out.println("msg:"+msg);
				if(msg.startsWith("<#ACCEPT#>"))
				{//收到可以加入信息
					String numStr=msg.substring(10);
					selfNum=Integer.parseInt(numStr);
					father.hd.sendEmptyMessage(0);
					//向服务器端发送上一局的得分情况
					dout.writeUTF("<#SCORE#>"+selfNum+"|"+Constant.SCORE);
				}
				else if(msg.startsWith("<#START#>"))
				{//收到开始游戏加入信息				
				   	new Thread()
				   	{
				   		public void run()
				   		{
				   			GameView.initBitmap(father.getResources()); 
					    	GameView.initCards(father.getResources());					
							WLQPActivity.cardListStr=msg.substring(9);
							father.hd.sendEmptyMessage(1);
				   		} 
				   	}.start();
				}
				else if(msg.startsWith("<#YOU#>"))
				{//获得牌权				
					perFlag=true;
				}
				else if(msg.startsWith("<#CURR#>"))
				{//知道了上一次玩家			<#CURR#>+玩家编号	
					lastNum=Integer.parseInt(msg.substring(8));					
				}
				else if(msg.startsWith("<#CARDS#>"))
				{//得到上一次玩家出的牌的信息
					lastCards=msg.substring(9);
				}
				else if(msg.startsWith("<#COUNT#>"))
				{//得到以<#COUNT#>为开头的信息  
					String temps=msg.substring(9);
					String[] ta=temps.split("\\,");
					int tempNum=Integer.parseInt(ta[1]);
					int tempCount=Integer.parseInt(ta[0]);
					
					if(tempNum!=selfNum)
					{
						int ifShang=((tempNum+1)>3)?1:(tempNum+1);
						if(ifShang==selfNum)
						{
							shangjiaCount=tempCount;
						}
						
						int ifXia=((tempNum-1)==0)?3:(tempNum-1);
						if(ifXia==selfNum)
						{
							xiajiaCount=tempCount;
						}
					}					
				}
				else if(msg.startsWith("<#FINISH#>"))
				{//得到游戏结束的信息
					int tempNum=Integer.parseInt(msg.substring(10));
					if(tempNum==selfNum)
					{
						father.hd.sendEmptyMessage(2);
					}
					else
					{
						father.hd.sendEmptyMessage(3);
					}
					
					this.father.gameview.viewdraw.flag=false;
					
					this.flag=false;
					this.din.close();
					this.dout.close();
					this.sc.close();
				}
				else if(msg.startsWith("<#EXIT#>"))
				{//得到有玩家退出游戏结束
					father.hd.sendEmptyMessage(4);
					this.father.gameview.viewdraw.flag=false;					
					this.flag=false;
					this.din.close();
					this.dout.close();
					this.sc.close();
				}
				else if(msg.startsWith("<#FULL#>"))
				{//玩家已满
					father.hd.sendEmptyMessage(5);
					this.father.gameview.viewdraw.flag=false;					
					this.flag=false;
					this.din.close();
					this.dout.close();
					this.sc.close();
				}
				else if(msg.startsWith("<#SCORE#>"))
				{//得分情况情况
					String ts=msg.substring(9);
					String[] sat=ts.split("\\|");
					scores[Integer.parseInt(sat[0])-1]=Integer.parseInt(sat[1]);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}