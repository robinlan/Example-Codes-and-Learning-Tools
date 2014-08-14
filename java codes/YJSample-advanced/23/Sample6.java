import mybeans.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Sample6 extends HttpServlet
{
   public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException
   {
      try{
         // 取得表單資料
         String tmp = request.getParameter("cars");
         String carname = new String(tmp.getBytes("ISO-8859-1"), "UTF-8");
         
         // 建立Bean
         CarBean cb = new CarBean();
         cb.setCarname(carname);
         cb.makeCardata();
      
         // 設定請求
         request.setAttribute("cb", cb);

         // 取得Servlet Context
         ServletContext sc = getServletContext();

         // 轉交請求
         if(carname.length() != 0){
            sc.getRequestDispatcher("/Sample6.jsp")
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