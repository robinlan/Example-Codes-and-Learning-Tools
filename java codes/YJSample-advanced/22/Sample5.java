import javax.servlet.*;
import javax.servlet.http.*;

public class Sample5 extends HttpServlet
{
   public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException
   {
      try{
         // 取得表單資料
         String carname = request.getParameter("cars");

         // 取得Servlet Context
         ServletContext sc = getServletContext();

         // 轉交請求
         if(carname.length() != 0){
            sc.getRequestDispatcher("/thanks.html")
              .forward(request, response);
         }
         else{
            sc.getRequestDispatcher("/error.html")
              .forward(request, response);
         }
      }
      catch(Exception e){    
         e.printStackTrace();
      }
   } 
}