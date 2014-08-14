import mybeans.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Sample5 extends HttpServlet
{
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response)
   throws ServletException
   {
      try{
         // 取得Servlet Context
         ServletContext sc = getServletContext();

         // 建立Bean
         CarDBBean cb = new CarDBBean();
      
         // 設定在請求中
         request.setAttribute("cb", cb);

         // 轉交請求
         sc.getRequestDispatcher("/Sample5.jsp")
           .forward(request, response);
      }
      catch(Exception e){    
         e.printStackTrace();
      }
   } 
}