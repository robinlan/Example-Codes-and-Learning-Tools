import mybeans.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SampleP3 extends HttpServlet
{
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response)
   throws ServletException
   {
      try{
         // 取得表單資料
         String tmp = request.getParameter("cars");
         String carname = new String(tmp.getBytes("8859_1"), "UTF-8");
         
         // 建立Bean
         CarBean cb = new CarBean();
         cb.setCarname(carname);
         cb.makeCardata();
      
         // 設定請求
         request.setAttribute("cb", cb);

         // 取得Servlet Context
         ServletContext sc = getServletContext();

         // 轉交請求
         if(carname.length() == 0){
            sc.getRequestDispatcher("/error.html")
              .forward(request, response);
         }
         else if(carname.equals("計程車")){
            sc.getRequestDispatcher("/SampleP3T.jsp")
              .forward(request, response);
         }
         else{
            sc.getRequestDispatcher("/SampleP3.jsp")
              .forward(request, response);
         }
      }
      catch(Exception e){    
         e.printStackTrace();
      } 
   } 
}