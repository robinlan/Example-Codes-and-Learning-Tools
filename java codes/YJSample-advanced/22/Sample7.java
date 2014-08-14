import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Sample7 extends HttpServlet
{
   public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException
   {
      try{
         // 設定內容類型
         response.setContentType("text/html; charset=UTF-8");

         // 輸出HTML文件
         PrintWriter pw = response.getWriter();
         pw.println("<br/>\n" +
                    "請選擇一件商品。<br/>\n" +
                    "<a href=\"../car1.html\">汽車</a><br/>\n" +
                    "<a href=\"../car2.html\">卡車</a><br/>\n" +
                    "<a href=\"../car3.html\">戰車</a><br/>\n");
       }
       catch(Exception e){    
          e.printStackTrace();
       }
   } 
}