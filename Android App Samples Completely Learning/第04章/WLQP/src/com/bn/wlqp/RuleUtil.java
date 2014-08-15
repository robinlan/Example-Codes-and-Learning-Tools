package com.bn.wlqp;
public class RuleUtil
{
   public static final int DAN_ZHANG=0;//单张牌
   public static final int DUI_ZI=1;   //对子
   public static final int SAN_ZHANG=2; //三张
   public static final int ZHA_DAN=3;   //炸弹
   public static final int N_A=5;   //不支持   
   public static final int TUO_LA_JI=4;   //不支持  
   
   //0 代表可以出牌  1  代表不能出牌 2 待定
   public static final int[][] CARDS_ROW_COL=
   {
   	   {2,1,1,1,1},
   	   {1,2,1,1,1},
   	   {1,1,2,1,1},
   	   {0,0,0,2,0},
   	   {1,1,1,1,2}
   };
   
   //检查是否符合出牌条件
   public static int ruleSelf(String curr)
   {
	   int result=N_A;
	   
	   String[] sa=curr.split("\\,");
	   int[] cards=new int[sa.length];
	   
	   for(int i=0;i<sa.length;i++)
	   {
		   try
		   {
			   cards[i]=Integer.parseInt(sa[i]);
		   }
		   catch(Exception e)
		   {
			   return N_A;
		   }
	   }
	   switch(sa.length)
	   {
	   	  case 1:;  //单张牌
	   	    result=DAN_ZHANG;
	   	  break;
	   	  case 2:  //对子
	   	    if(((cards[0]<52)&&(cards[1]<52))&&((cards[0]%13)==(cards[1]%13)))
	   	    {
	   	    	result=DUI_ZI;
	   	    }
	   	    else
	   	    {
	   	    	result=N_A; //不支持
	   	    }
	   	  break;
	   	  case 3: //三张
	   	    if(((cards[0]<52)&&(cards[1]<52)&&(cards[2]<52))&&
	   	       (((cards[0]%13)==(cards[1]%13))&&((cards[1]%13)==(cards[2]%13))))
	   	    {
	   	       	result=SAN_ZHANG;
	   	    }
	   	    else
	   	    {
	   	    	result=N_A; //不支持
	   	    }
	   	  break;
	   	  case 4: //炸弹
	   	    if(((cards[0]<52)&&(cards[1]<52)&&(cards[2]<52)&&(cards[3]<52))&&
	   	       (((cards[0]%13)==(cards[1]%13))&&((cards[1]%13)==(cards[2]%13))
	   	         &&((cards[2]%13)==(cards[3]%13))))
	   	    {
	   	       	result=ZHA_DAN;
	   	    }
	   	    else if(((cards[0]<52)&&(cards[1]<52)&&(cards[2]<52)&&(cards[3]<52))&&
	   	            (((cards[0]%13)==(cards[1]%13))&&((cards[2]%13)==(cards[3]%13))&&(Math.abs((cards[0]%13)-(cards[2]%13))==1))||
	   	            (((cards[0]%13)==(cards[2]%13))&&((cards[1]%13)==(cards[3]%13))&&(Math.abs((cards[0]%13)-(cards[1]%13))==1))||
	   	            (((cards[0]%13)==(cards[3]%13))&&((cards[2]%13)==(cards[1]%13))&&(Math.abs((cards[0]%13)-(cards[2]%13))==1)))
	   	    {
	   	    	
	   	    	result=TUO_LA_JI;
	   	    }
	   	    else
	   	    {
	   	    	result=N_A; //不支持
	   	    }
	   	  break;
	   }	   	   
	   
	   return result;
   }
   
   public static boolean rule(String last,String curr)
   {
	   if(last==null)
	   {
		   if(ruleSelf(curr)!=N_A)
		   {
			   return true;
		   }
		   else
		   {
			   return false;
		   }
	   }
	   
	   if(curr.length()==0)
	   {
		   return false;
	   }  
	  
     	//上一个玩家发的牌
   	  int currState=ruleSelf(curr);
   	  String[] sb=last.split("\\,");
	  int[] lastCards=new int[sb.length];
	   
	  for(int i=0;i<sb.length;i++)
	  {
		  lastCards[i]=Integer.parseInt(sb[i]);
	  }
	  
	  //本家要发的牌
	  String[] sc=curr.split("\\,"); 
	  int[] currCards=new int[sc.length];
	   
	  for(int i=0;i<sc.length;i++)
	  {
		  currCards[i]=Integer.parseInt(sc[i]);
	  }
	  
	  
   	  if(currState==N_A)
   	  {
   	  	 return false;
   	  }
   	  
   	  int lastState=ruleSelf(last);
   	  
   	  int resultCase=CARDS_ROW_COL[currState][lastState];
   	  
   	  if(resultCase==0)
   	  {
   	  	return true;
   	  }
   	  
   	  if(resultCase==1)
   	  {
   	  	 return false;
   	  }
   	  
   	  
   	  boolean result=true;
   	  switch(currState)
   	  {
   	  	 case DAN_ZHANG://判断单张牌
   	  	   System.out.println("DAN_ZHANG");
   	  	   System.out.println("lastCards[0]"+lastCards[0]+":::"+"currCards[0]"+currCards[0]);
   	  	   if(((lastCards[0]<52)&&(currCards[0]<52)&&(lastCards[0]%13<currCards[0]%13))||
   	  	       ((lastCards[0]<52)&&(currCards[0]>=52))||
   	  	       (lastCards[0]>=52)&&(currCards[0]>=52)&&(lastCards[0]<currCards[0]))
   	  	   {   	  	   	  
   	  	   	  result=true;  
   	  	   }
   	  	   else
   	  	   {
   	  	   	  result=false;
   	  	   }
   	  	 break;
   	  	 case DUI_ZI://判断对子
   	  	 System.out.println("DUI_ZI");
   	  	   if(lastCards[0]%13<currCards[0]%13)
   	  	   {
   	  	   	  result=true;
   	  	   }
   	  	   else
   	  	   {
   	  	   	  result=false;
   	  	   }
   	  	 break;
   	  	 case SAN_ZHANG://判断三张牌
   	  	 System.out.println("SAN_ZHANG");
   	  	   if(lastCards[0]%13<currCards[0]%13)
   	  	   {
   	  	   	  result=true;
   	  	   }
   	  	   else
   	  	   {
   	  	   	  result=false;
   	  	   }
   	  	 break;
   	  	 case ZHA_DAN://判断炸弹
   	  	 System.out.println("ZHA_DAN");
   	  	   if(lastCards[0]%13<currCards[0]%13)
   	  	   {
   	  	   	  result=true;
   	  	   }
   	  	   else
   	  	   {
   	  	   	  result=false;
   	  	   }
   	  	 break;
   	  	 case TUO_LA_JI://判断拖拉机
   	  	 
   	  	 int[] resulta=getSmall(lastCards);
   	  	 int[] resultb=getSmall(currCards);
   	  	 
   	  	 System.out.println("TUO_LA_JI");
   	  	   if(resulta[0]%13<resultb[0]%13)
   	  	   {
   	  	   	  result=true;
   	  	   }
   	  	   else
   	  	   {
   	  	   	  result=false;
   	  	   }
   	  	 break;
   	  }
   	  return result;
   }
   public static int[] getSmall(int[] result)
   {
	   for(int i=0;i<result.length-1;i++){
	   int t;
	   for(int j=i+1;j<result.length;j++)
	   {
		   if(result[i]>result[j])
		   {
		      t=result[i];result[i]=result[j];result[j]=t;
		   }
	   }
	   }
	  return result;
   
   }
}