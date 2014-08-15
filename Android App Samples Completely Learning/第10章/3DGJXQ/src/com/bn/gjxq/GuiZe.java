package com.bn.gjxq;
/**
 * 該類是國際象棋的規則類，其他類通過調用canMove方法給出起始位置與結束位置
 */
enum Finish {NO_FINISH,BLACK_WIN,WHITE_WIN}
public class GuiZe
{	
	public static boolean canMove(ChessForControl chessforcontrol,ChessForControl[][] currBoard,int toZ,int toX)//行,列
	{
		int row=chessforcontrol.row;//當前對象的行
		int col=chessforcontrol.col;//當前對象的列
		if(//如果該位置是自己方的，那麼直接不可下
			    currBoard[toZ][toX]!=null
				&&currBoard[toZ][toX].chessType>=(chessforcontrol.chessType/6)*6
				&&currBoard[toZ][toX].chessType<((chessforcontrol.chessType/6)+1)*6
		   )
			{
				return false;
			}
		switch(chessforcontrol.chessType)//根據對象的類型進行分支判斷
		{
		case 0://黑車
		case 6://白車
			if((chessforcontrol.row==toZ&&!containHor(chessforcontrol,currBoard,toZ,toX))||
					(chessforcontrol.col==toX&&!containVer(chessforcontrol,currBoard,toZ,toX))
			  )
			{
				return true;
			}
			break;
		case 1:
		case 7://當前為馬
		
			if(Math.abs(chessforcontrol.row-toZ)+Math.abs(chessforcontrol.col-toX)==3)
			{
				if(chessforcontrol.row==toZ||chessforcontrol.col==toX)
				{
					return false;
				}
				return true;
			}
			break;
		
		case 2:
		case 8://當前為象
			if(!containSlant(chessforcontrol,currBoard,toZ,toX))
			{
				return true;
			}
			break;
		case 3:
		case 9://當前為後
			if((chessforcontrol.row==toZ&&!containHor(chessforcontrol,currBoard,toZ,toX))||
					(chessforcontrol.col==toX&&!containVer(chessforcontrol,currBoard,toZ,toX))||
					(!containSlant(chessforcontrol,currBoard,toZ,toX))
			  )
			{
				return true;
			}
			break;
		case 4:
		case 10://當前為王
			if(Math.abs(toZ-row)<2&&Math.abs(toX-col)<2)
			{
				return true;
			}
           break;	
		case 5://黑兵		
			if(row+1<8&&currBoard[row+1][col]==null)//移動一格，
	  		{
	  			if(toZ==row+1&&toX==col)//目標格子就是這個格子
	  			{
	  				chessforcontrol.isMoved=true;
	  				return true;
	  			}
	  			else if(row+2<8&&!chessforcontrol.isMoved//兵沒有移動過，才能走兩格
	  					&&currBoard[row+2][col]==null//移動兩格的那個格子必須為空
	  					&&toZ==row+2&&toX==col)//目標位置為此位置
	  		  			{
	  				        chessforcontrol.isMoved=true;
	  		  				return true;
	  		  			}
	  		}
			if(row+1<8&&col+1<8&&currBoard[row+1][col+1]!=null//這個是為了斜走吃對方的，所以此格肯定不能為空
					&&currBoard[row+1][col+1].chessType>=((chessforcontrol.chessType/6+1)%2)*6
					&&currBoard[row+1][col+1].chessType<(((chessforcontrol.chessType/6)+1)%2+1)*6)//此格要是對方棋子，
			{
				if(toZ==row+1&&toX==col+1)//目標格子就是這個格子
	  			{
					chessforcontrol.isMoved=true;
	  				return true;
	  			}
			}
			if(row+1<8&&col-1>=0&&currBoard[row+1][col-1]!=null//這個是為了斜走吃對方的，所以此格肯定不能為空
					&&currBoard[row+1][col-1].chessType>=((chessforcontrol.chessType/6)+1)%2*6
					&&currBoard[row+1][col-1].chessType<(((chessforcontrol.chessType/6)+1)%2+1)*6)//此格要是對方棋子，
			{
				if(toZ==row+1&&toX==col-1)//目標格子就是這個格子
	  			{
					chessforcontrol.isMoved=true;
	  				return true;
	  			}
			}
			break;
		case 11://當前為白兵		
			if(row-1>0&&currBoard[row-1][col]==null)//移動一格，
	  		{
	  			if(toZ==row-1&&toX==col)//目標格子就是這個格子
	  			{
	  				chessforcontrol.isMoved=true;
	  				return true;
	  			}
	  			else if(row-2>0&&!chessforcontrol.isMoved//兵沒有移動過，才能走兩格
	  					&&currBoard[row-2][col]==null//移動兩格的那個格子必須為空
	  					&&toZ==row-2&&toX==col)//目標位置為此位置
	  		  			{
	  						chessforcontrol.isMoved=true;
	  		  				return true;
	  		  			}
	  		}
			if(row-1>0&&col+1<8&&currBoard[row-1][col+1]!=null//這個是為了右斜走吃對方的，所以此格肯定不能為空
					&&currBoard[row-1][col+1].chessType>=((chessforcontrol.chessType/6)+1)%2*6
					&&currBoard[row-1][col+1].chessType<(((chessforcontrol.chessType/6)+1)%2+1)*6)//此格要是對方棋子，
			{
				if(toZ==row-1&&toX==col+1)//目標格子就是這個格子
	  			{
					chessforcontrol.isMoved=true;
	  				return true;
	  			}
			}
			if(row-1>0&&col-1>=0&&currBoard[row+1][col-1]!=null//這個是為了左斜走吃對方的，所以此格肯定不能為空
					&&currBoard[row-1][col-1].chessType>=(((chessforcontrol.chessType/6)+1)%2)*6
					&&currBoard[row-1][col-1].chessType<(((chessforcontrol.chessType/6)+1)%2+1)*6)//此格要是對方棋子，
			{
				if(toZ==row-1&&toX==col-1)//目標格子就是這個格子
	  			{
					chessforcontrol.isMoved=true;
	  				return true;
	  			}
			}			
			break;
		}
		return false;
    }
	public static Finish isFinish(ChessForControl[][] currBoard)//判斷某家是否贏了
	{
		boolean black=false;
		boolean white=false;
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				if(currBoard[i][j]!=null)
				{
					if(currBoard[i][j].chessType==4)//如果當前是黑王
					{
						black=true;
						
					}
					else if(currBoard[i][j].chessType==10)//如果當前是白王
					{
						white=true;
					}
				}
			}
		}		
		if(!black)//如果是黑方輸了
		{
			return Finish.WHITE_WIN;//返回白方贏
		}
		else if(!white)//如果是白方輸了
		{
			return Finish.BLACK_WIN;//返回黑方贏
		}
		return Finish.NO_FINISH;//否則返回沒有輸贏
	}

//水平兩點之間含有棋子
public static boolean containHor(ChessForControl chessforcontrol,ChessForControl[][] currBoard,int row,int col)
{
	if(col>chessforcontrol.col)//如果目標點在當前對像右邊
	{
		for(int i=chessforcontrol.col+1;i<col;i++)
		{
			if(currBoard[row][i]!=null)//當前點不為空
			{
				return true;//含有子
			}
		}
	}
	else//如果目標點在當前對像左邊
	{
		for(int i=col+1;i<chessforcontrol.col;i++)
		{
			if(currBoard[row][i]!=null)//當前點不為空
			{
				return true;//含有子
			}
		}
	}
	return false;
 }
//垂直方向上還有棋子
public static boolean containVer(ChessForControl chessforcontrol,ChessForControl[][] currBoard,int row,int col)
{
	if(chessforcontrol.row<row)//目標點在當前子下方
	{
		for(int i=chessforcontrol.row+1;i<row;i++)
		{
			if(currBoard[i][col]!=null)
			{
				return true;
			}
		}
	}
	else//目標點在當前子上方
	{
		for(int i=row+1;i<chessforcontrol.row;i++)
		{
			if(currBoard[i][col]!=null)
			{
				return true;
			}
		}
	}
	
	return false;
}
//判斷斜方向上是否含有棋子
public static boolean containSlant(ChessForControl chessforcontrol,ChessForControl[][] currBoard,int row,int col)
{
	if(chessforcontrol.row-row+chessforcontrol.col-col==0)//撇
	{
		if(chessforcontrol.col>col)//如果目標點位置在當前位置左邊
		{
			for(int i=col+1;i<chessforcontrol.col;i++)
			{
				if(currBoard[row+col-i][i]!=null)
				{
					return true;
				}
			}
		}
		if(chessforcontrol.col<col)//如果目標點在當前位置右邊
		{
			for(int i=chessforcontrol.col+1;i<col;i++)
			{
				if(currBoard[col+row-i][i]!=null)
				{
					return true;
				}
			}
		}
	}
	else if(chessforcontrol.col-chessforcontrol.row==col-row)//如果在斜線上-----捺
		{
			if(col<chessforcontrol.col)//如果目標點在當前位置左邊
			{
				for(int i=col+1;i<chessforcontrol.col;i++)
				{
					if(currBoard[row-col+i][i]!=null)
					{
						return true;
					}
				}
			}
			if(col>chessforcontrol.col)//如果目標點在當前點右邊
			{
				for(int i=chessforcontrol.col+1;i<col;i++)
				{
					if(currBoard[row-col+i][i]!=null)//
					{
						return true;
					}
				}
			}
			
		}
	return false;
}
}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	
