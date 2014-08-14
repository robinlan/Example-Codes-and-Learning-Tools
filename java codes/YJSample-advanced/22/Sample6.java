import javax.servlet.*;
import javax.servlet.http.*;

public class Sample6 extends HttpServlet
{
   public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException
   {
      try{
         // 取得表單資料
         String carname = request.getParameter("cars");

         // 取得ServletContext
         ServletContext sc = getServletContext();

         // 轉交請求
         if(carname.length() != 0){
            sc.getRequestDispatcher("/servlet/Sample2")
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