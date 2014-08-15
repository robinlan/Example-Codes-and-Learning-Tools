package wyf.wyy;
import java.io.*; 
import java.util.*;
import javax.servlet.*; 
import javax.servlet.http.*;
public class OrderServlet extends HttpServlet{
	public void init(ServletConfig conf) throws ServletException 
	{ //Servlet的inti初始化方法
		super.init(conf);
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException, IOException{//doGet方法
		doPost(req,res);//調用doPost方法
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException, IOException{
		req.setCharacterEncoding("big5");
		res.setCharacterEncoding("big5");		
		String action = req.getParameter("action");//得到請求的響應action		
		HttpSession session=req.getSession(true);//得到session對像
		PrintWriter pw=res.getWriter();//得到輸出流對像
		String msg="";//聲明提示消息		
		String uname = (String)session.getAttribute("uname");//得到登陸用戶名
		//資源的幾種狀態
		String ostatus1 = "預訂中";
		String ostatus2 = "預訂成功";
		String ostatus3 = "預訂失敗";
		Vector<String[]> OrderList = //得到訂單列表
					(Vector<String[]>)session.getAttribute("OrderList");
		if(OrderList==null)//如果為空則創建一個訂單列表對像
		{OrderList = new Vector<String[]>();}		
		/*if(action.equals("ADD")){	
			String orderNum = (String)req.getParameter("orderNum");//得到資源名		
			if(orderNum!=null){//得到預訂開始時間
				String fyear = (String)req.getParameter("fyear");
				String fmonth = (String)req.getParameter("fmonth");
				String fday = (String)req.getParameter("fday");
				String fhour = (String)req.getParameter("fhour");
				//得到預訂結束時間
				String eyear = (String)req.getParameter("eyear");
				String emonth = (String)req.getParameter("emonth");
				String eday = (String)req.getParameter("eday");
				String ehour = (String)req.getParameter("ehour");
				//得到所預訂的資源分組
				String group = (String)req.getParameter("group");
				String ftime = fyear+"-"+fmonth+"-"+fday+"-"+fhour+":"+"00";
				String etime = eyear+"-"+emonth+"-"+eday+"-"+ehour+":"+"00";				
				String[] s = new String[4];
				s[0] = orderNum; s[1] = group;
				s[2] = ftime;    s[3] = etime;				
				OrderList.add(s);//將訂單加入到訂單列表中				
				session.setAttribute("OrderList",OrderList);//將訂單列表放到session								
			}
			res.sendRedirect("groupList.jsp");//發回到groupList頁面
		}
		else if(action.equals("DELETE")){			
			String id = req.getParameter("index");//得到要刪除訂單的下標			
			int index = Integer.parseInt(id);//轉換為int值			
			OrderList.remove(index);//刪除此選中的訂單
			res.sendRedirect("myOrder.jsp");//發回訂單頁面
		}
		else if(action.equals("REMOVE")){		
			OrderList.removeAllElements();//移除所有訂單			
			session.setAttribute("OrderList",OrderList);//將訂單列表放進session			
			res.sendRedirect("myOrder.jsp");//發回訂單頁面			
		}
		else if(action.equals("SUBMIT")){			
			if(uname!=null){//判斷用戶是否登陸
				if(Order_DB.addOrder(uname,OrderList)!=-1){					
					OrderList.removeAllElements();//提交完成，清空訂單列表
					msg = "訂單提交成功，本酒店歡迎您的光臨<br><br>"+
					       "<a href=main.jsp>返回主頁";//給出成功消息
				}
				else{
					msg = "對不起，系統錯誤，提交失敗！<br><br>"+
					       "<a href=main.jsp>返回主頁";//給出失敗消息
				}			
			}
			else{//用戶沒有登陸的情況
				msg = "您還沒有登陸，請先登陸。<br><br>"+
				     "<a href=login.jsp>現在登陸>>";		
			}
			req.setAttribute("msg",msg);//將消息發送到消息顯示頁面
			req.getRequestDispatcher("info.jsp").forward(req,res);	
		}
		else if(action.equals("order")){
			if(uname==null){//用戶沒有登陸
				msg = "您還沒有登陸，請先登陸。<br><br>"+
				     "<a href=login.jsp>現在登陸>>";
				req.setAttribute("msg",msg);
				req.getRequestDispatcher("info.jsp").forward(req,res);
			}
			else{//查詢該用戶的所有訂單信息
				String sql = "select * from olist where oname='"+uname+"'";
				Vector<String []> list = Order_DB.getOrderList(sql);//執行查詢				
				req.setAttribute("list",list);
				req.getRequestDispatcher("list.jsp").forward(req,res);
			}
		}*/
		if(action.equals("ListDetail")){
			String oid = req.getParameter("oid");//得到訂單編號
			Vector<String []> ListDetail = Order_DB.getOrderDetail(oid);//執行查詢				
			req.setAttribute("ListDetail",ListDetail);
			req.setAttribute("oid",oid);
			req.getRequestDispatcher("detail.jsp").forward(req,res);
		}
		
		else if(action.equals("allOrders")){//按條件查詢訂單
			if(session.getAttribute("adname")!=null){//管理員是否登陸
				String sql = "";//聲明SQL引用
				int conditon = Integer.parseInt(req.getParameter("condition"));
				switch(conditon){
					case 1://1表示所有訂單
					sql = "select * from olist";
					break;
					case 2://2表示已經處理的訂單
					sql = "select * from olist where ostatus='"+ostatus2+"' or ostatus='"+ostatus3+"'";
					break;
					case 3://3表示未處理的訂單
					sql = "select * from olist where ostatus='"+ostatus1+"'";
					break;
				}
				Vector<String []> list = Order_DB.getOrderList(sql);				
				req.setAttribute("list",list);//將訂單列表返回				
				req.getRequestDispatcher("adminOrders.jsp").forward(req,res);
			}
			else{
				msg = "請先登陸";//沒有登陸的提示消息
				req.setAttribute("msg",msg);
				req.getRequestDispatcher("adinfo.jsp").forward(req,res);
			}
		}
		else if(action.equals("query")){//按編號查詢訂單
		    Vector<String []> list = null;
		    try{
		    	int oid = Integer.parseInt(req.getParameter("oid"));
				String sql = "select * from olist where oid="+oid;
				list = Order_DB.getOrderList(sql);
		    }
		    catch(NumberFormatException nfe)//輸入訂單號格式不正確
		    {list = new Vector<String []>();}//返回一個空的向量							
			req.setAttribute("list",list);
			req.getRequestDispatcher("adminOrders.jsp").forward(req,res);
		}
		else if(action.equals("dealOrder")){//處理訂單
			String adname = (String)session.getAttribute("adname");
			String reason = req.getParameter("reason");
			String ostatus = req.getParameter("ostatus");
			int oid = Integer.parseInt(req.getParameter("oid"));
			//拼裝SQL
			String sqla = "update olist set ostatus='"+ostatus+"',oreason='"+
							reason+"',odeal='"+adname+"'where oid="+oid;
			String sqlb = "update oinfo set ostatus='"+ostatus+"' where oid="+oid;			
			boolean b = DB.update(sqla,sqlb);//執行更新		
			if(b==true){
				msg = "訂單處理成功<br><br>"
					+"<a href=OrderServlet?action=allOrders&&condition=1>返回";
			}
			else{msg = "訂單處理發生錯誤，處理失敗";}
			req.setAttribute("msg",msg);//返回處理消息
			req.getRequestDispatcher("adinfo.jsp").forward(req,res);
		}
	}
}
