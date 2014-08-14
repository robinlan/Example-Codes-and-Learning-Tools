import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class Sample7 extends HttpServlet
{
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response)
   throws ServletException
   {
      try{
         // 容器類型的設定
         response.setContentType("text/html; charset=UTF-8");
        
         // 進行輸出
         PrintWriter pw = response.getWriter();

         StreamSource in = new StreamSource("http://localhost:8080/YJKSample10/Sample.xml");
         StreamSource ss = new StreamSource("http://localhost:8080/YJKSample10/Sample2.xsl");
         StreamResult out = new StreamResult(pw);

         TransformerFactory tff = TransformerFactory.newInstance();
         Transformer tf = tff.newTransformer(ss);
         tf.transform(in, out);

       }
       catch(Exception e){    
          e.printStackTrace();
       }
   } 
}