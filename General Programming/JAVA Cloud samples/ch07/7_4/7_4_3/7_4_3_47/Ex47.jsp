<%@ page contentType= "text/html;charset=big5" %>
<%@ page import = "java.io.*" %>
<html>
<head><title>Ex47</title></head><body>
<%
  request.setCharacterEncoding("big5");
  String val_fA = request.getParameter("f_A");
  String val_fB = request.getParameter("f_B");

  int msgInt_A, msgInt_B;
  char msgCh_A, msgCh_B;
  String msgStr_A= "", msgStr_B= "";

  BufferedReader bfRead_A = new BufferedReader(new FileReader(val_fA));
  BufferedReader bfRead_B = new BufferedReader(new FileReader(val_fB));

  while ((msgInt_A = bfRead_A.read()) != -1) {
        msgCh_A = (char)msgInt_A;
        msgStr_A = msgStr_A + msgCh_A;
  }
  out.print("成功讀取檔案A" + "<br>");

  while ((msgInt_B = bfRead_B.read()) != -1) {
        msgCh_B = (char)msgInt_B;
        msgStr_B = msgStr_B + msgCh_B;
  }
  out.print("成功讀取檔案B" + "<br>");

  bfRead_A.close();
  bfRead_B.close();

  BufferedWriter bfWrite_B = new BufferedWriter(new FileWriter(val_fB));

  bfWrite_B.write(msgStr_A + msgStr_B);
  out.print("成功傳遞檔案A至檔案B" + "<br>");

  bfWrite_B.close();
%>
</body>
</html>