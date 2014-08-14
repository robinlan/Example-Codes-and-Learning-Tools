import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Sample3 extends HttpServlet
{
   public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException
   {
      try{
         // 取得表單資料
         String tmp = request.getParameter("cars");
         String carname = new String(tmp.getBytes("ISO-8859-1"), "UTF-8");
      
         // 設定內容類型
         response.setContentType("text/html; charset=UTF-8");

         // 輸入HTML文件
         PrintWriter pw = response.getWriter();
         if(carname.length() != 0){
            pw.println("<html>\n" +
                       "<head><title>\n" + carname + "</title></head>\n" +
                       "<body><center>\n" +
                       "<h2>\n" + carname + "</h2>\n" +
                       "感謝您購買本公司的" + carname + "。<br/>\n" +
                       "</center></body>\n" +
                       "</html>\n");
          }
          else{
             pw.println("<html>\n" +
                        "<head><title>錯誤</title></head>\n" +
                        "<body><center>\n" +
                        "<h2>錯誤</h2>\n" +
                        "<br/>請輸入商品名稱。\n" +
                        "</center></body>\n" +
                        "</html>\n");
          }
       }
       catch(Exception e){    
          e.printStackTrace();
       }
   } 
}