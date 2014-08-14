import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SampleP1 extends HttpServlet
{
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response)
   throws ServletException
   {
      try{
         // 取得表單資料
         String tmp = request.getParameter("name");
         String name = new String(tmp.getBytes("8859_1"), "UTF-8");

         // 設定內容類型
         response.setContentType("text/html; charset=UTF-8");

         // 輸出HTML文件
         PrintWriter pw = response.getWriter();
         if(name.length() != 0){
            pw.println("<html>\n" +
                       "<head><title>\n" + name + "</title></head>\n" +
                       "<body><center>\n" +
                       "<h2>歡迎</h2>\n" +
					   "歡迎光臨，" +
                       name + "先生。<br/>\n" +
                       "</center></body>\n" +
                      "</html>\n");
          }
          else{
             pw.println("<html>\n" +
                        "<head><title></title></head>\n" +
                        "<body><center>\n" +
                        "<h2>錯誤</h2>\n" +
                        "請輸入您的名字。<br/>\n" +
                        "</center></body>\n" +
                        "</html>\n");
          }
       }
       catch(Exception e){
          e.printStackTrace();
       }
   } 
}