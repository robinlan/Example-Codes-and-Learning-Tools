package com.bn.gjxq;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
public class ClientAgent extends Thread
{
	GJXQActivity father;//Activity引用
	Socket sc;
	DataInputStream din;//輸入流
	DataOutputStream dout;//輸出流
	boolean flag=true;//是否結束客服端線程
	int num;//當前編號
	boolean perFlag=false;//是否為自己走棋標誌位,true為自己走,false為對方走
	
	public ClientAgent(GJXQActivity father,Socket sc,DataInputStream din,DataOutputStream dout)
	{
		this.father=father;//activity的引用
		this.sc=sc;//Socket引用
		this.din=din;//輸入流
		this.dout=dout;//輸出流
	}
	public void run()
	{
		while(flag)
		{
			try
			{
				String msg=din.readUTF();//等待消息
				if(msg.startsWith("<#ACCEPT#>"))//如果是成功的加入了遊戲,那麼進入等待界面
				{
					String numStr=msg.substring(10);
					num=Integer.parseInt(numStr);//得到當前的自己的角色
					father.hd.sendEmptyMessage(0);//發轉換等待界面消息	
				}
				else if(msg.startsWith("<#START#>"))//如果是第二個玩家進入,那麼進入3D界面
				{
					father.hd.sendEmptyMessage(1);//發進入3d界面消息
				}
				else if(msg.startsWith("<#PERMISIION#>"))//每次下完棋子後查看玩家是否有輸贏的情況
				{
					perFlag=true;
					if(father.msv!=null)
					{
						switch(GuiZe.isFinish(father.msv.currBoard))
						{
						  case BLACK_WIN:
							 dout.writeUTF("<#FINISH#>0");//如果是黑方贏了
						  break;
						  case WHITE_WIN:
							 dout.writeUTF("<#FINISH#>1");///如果是白方贏了
						  break;
						}
					}
				}
				else if(msg.startsWith("<#MOVE#>"))//移動標誌,
				{
					String temps=msg.substring(8);
					String[] sa=temps.split("\\,");
					int srcRow=Integer.parseInt(sa[0]);
	      			int srcCol=Integer.parseInt(sa[1]);
	      			int dstRow=Integer.parseInt(sa[2]);
	      			int dstCol=Integer.parseInt(sa[3]);
	      			
	      			ChessForControl[][] currBoard=father.msv.currBoard;//拿到棋盤的棋子的引用
	      			currBoard[srcRow][srcCol].y=0;//將棋子的高度設為零
	    			currBoard[srcRow][srcCol].row=dstRow;//把其位置設為新位置
	    			currBoard[srcRow][srcCol].col=dstCol;
	    			currBoard[dstRow][dstCol]=currBoard[srcRow][srcCol];//把其引用指向原來的位置的對象
	    			currBoard[srcRow][srcCol]=null;//原來位置的數組引用捨為空
	    			father.playSound(1, 0);//播放移動聲音
				}
				else if(msg.startsWith("<#FINISH#>"))//如果是接受到輸贏信息了.
				{
					father.msv=null;
					int pTemp=Integer.parseInt(msg.substring(10));
					if(pTemp==0&&this.num==1||pTemp==1&&this.num==2)//如果是贏方
					{
						father.hd.sendEmptyMessage(2);
					}
					else//輸方
					{
						father.hd.sendEmptyMessage(3);
					}
					this.flag=false;
					this.din.close();//光掉輸入流
					this.dout.close();//關掉輸出流
					this.sc.close();//關掉相關的sc					
				}
				else if(msg.startsWith("<#FULL#>"))
				{
					this.flag=false;
					this.din.close();//光掉輸入流
					this.dout.close();//關掉輸出流
					this.sc.close();//關掉相關的sc	
					father.hd.sendEmptyMessage(7);
				}
				else if(msg.startsWith("<#EXIT#>"))
				{
					father.hd.sendEmptyMessage(4);
					this.flag=false;
					this.din.close();//光掉輸入流
					this.dout.close();//關掉輸出流
					this.sc.close();//關掉相關的sc	
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	//發送消息
	public void sendMessage(String msg)
	{
		try 
		{
			dout.writeUTF(msg);
		} catch (IOException e) 
		{			
			e.printStackTrace();
		}
	}
}

