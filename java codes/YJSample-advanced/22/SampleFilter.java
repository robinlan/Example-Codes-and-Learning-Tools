import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SampleFilter implements Filter 
{
   public void doFilter(ServletRequest request,
                        ServletResponse response,
                        FilterChain chain)
               throws IOException, ServletException
   {
      // 設定內容類型
      response.setContentType("text/html; charset=UTF-8");

      // 輸出HTML文件
      PrintWriter pw = response.getWriter();
      pw.println("<html>\n" +
                 "<head><title>範例</title></head>\n" +
                 "<body><center>\n" +
                 "<h2>您好</h2>" +
                 "<hr/>\n");

      chain.doFilter(request, response);

      pw.println("<hr/>謝謝惠顧。\n" +
                 "</center></body>\n" +
                 "</html>\n");

   }
   public void init(FilterConfig filterConfig){}
   public void destroy(){} 
}