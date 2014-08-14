<%@ page contentType="text/html; charset=Big5" %>
<%@ page import = "java.io.*" %>
<html>
<head><title>Ex92visitCounter</title></head><body>
<p align="left">
<font size="5"><b>Page of Ex92visitCounter</b></font>
</p>
<%
request.setCharacterEncoding("big5");

String fCount = request.getRealPath("/fileVisitor.txt");

//讀取檔案內曾儲存之拜訪人次
BufferedReader br = new BufferedReader(new FileReader(fCount));
String numStr = br.readLine();
br.close(); 

//將拜訪人次加1再儲存入檔案
if (session.isNew()) {
   int numInt = Integer.parseInt(numStr) + 1;
   numStr = String.valueOf(numInt);
   BufferedWriter bw = new BufferedWriter(new FileWriter(fCount));
   bw.write(String.valueOf(numStr));
   bw.close();
}

//使用圖形數字印出拜訪人次
out.print("本網頁拜訪人次： " + "<br>");
for(int i = 0; i < numStr.length(); i++)  {
    %>
    <img src = "./images/<%= numStr.charAt(i) %>.gif"></img>
    <%
}
%>
</body>
</html>
