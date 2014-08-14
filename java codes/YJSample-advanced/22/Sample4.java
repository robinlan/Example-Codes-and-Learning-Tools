import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Sample4 extends HttpServlet
{
   public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException
   {
      try{
         // 取得Session
         HttpSession hs = request.getSession(true);	
         Integer cn = (Integer) hs.getAttribute("count");	
         Date dt = (Date) hs.getAttribute("date");	

         String str1, str2;

         // 設定次數
         if(cn == null){
            cn = new Integer(1);
            dt = new Date();
            str1 = "這是您的初次造訪。";
            str2 = "";
         }
         else{
            cn = new Integer(cn.intValue() + 1);
            dt = new Date();
            str1 = "這是您的第" + cn + "次造訪。";
            str2 = "(上次是在：" + dt + ")";
         }
     
         // 設定Session
         hs.setAttribute("count", cn);
         hs.setAttribute("date", dt);

         // 設定內容類型
         response.setContentType("text/html; charset=UTF-8");

         // 輸出HTML文件
         PrintWriter pw = response.getWriter();
         pw.println("<html>\n" +
                    "<head><title>範例</title></head>\n" +
                    "<body><center>\n" +
                    "<h2>歡迎光臨</h2>" +
                    "<hr />\n" +
                    str1 + "<br/>\n" +
                    str2 + "<br/>\n" +
                    "請選擇一件商品。<br/>\n" +
                    "<br/>\n" +
                    "<a href=\"../car1.html\">汽車</a><br/>\n" +
                    "<a href=\"../car2.html\">卡車</a><br/>\n" +
                    "<a href=\"../car3.html\">戰車</a><br/>\n" +
                    "</center></body>\n" +
                    "</html>\n");
        }
        catch(Exception e){    
          e.printStackTrace();
       }
   } 
}