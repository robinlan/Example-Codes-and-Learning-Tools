<%@ page contentType="text/html;charset=big5"
    import="java.util.*,wyf.wyy.*"%>
 <html>
  <head>
   <title>資源管理</title>
   <link href="css/generalstyle.css" type="text/css" rel="stylesheet">
  </head>
 <body>
    <%@ include file="adminRestop.jsp" %>
	<% Vector<String> list = (Vector<String>)session.getAttribute("list");
	   int group = Integer.parseInt(list.get(0));
	   String gName = list.get(1);//得到分組名
	   String cpStr=request.getParameter("cp");
	   int currPage=1;//設置默認當前頁為第一頁
	   if(cpStr!=null){
	 	currPage=Integer.parseInt(cpStr.trim());//得到當前頁
	    }
	   int span=5;//每頁顯示記錄條數為5條
	   int totalPage=DB.getTotal(span,group);//得到總頁數
     %>
   <%@ include file="adminFenYe.jsp" %>
 </body>
</html>
