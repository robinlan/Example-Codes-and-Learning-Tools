<%@language=jscript%>
<%
fso=Server.CreateObject("Scripting.FileSystemObject");
parentPath=fso.GetParentFolderName(Request.ServerVariables("URL"));
title=fso.GetFileName(parentPath);
%>
<html>
<head>
	<title><%=title%></title>
	<meta HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=big5">
	<meta HTTP-EQUIV="Expires" CONTENT="0">
	<style>
		td {font-family: "標楷體", "helvetica,arial", "Tahoma"}
		A:link {text-decoration: none}
		A:hover {text-decoration: underline}
	</style>
</head>

<body background="/jang/graphics/background/yellow.gif">
<font face="標楷體">
<h2 align=center><%=title%></h2>
<h3 align=center><a href="mirlab.org/jang">張智星</a></h3>
<hr>

<%
absTopPath = Server.MapPath(".");
fd=fso.GetFolder(absTopPath);
%>
<table border=1 align=center>
<tr>
<th>範例列表</th>
<%
var fileList=new Enumerator(fd.files);
for (fileList.moveFirst(); !fileList.atEnd(); fileList.moveNext()){
	fileName=fileList.item().name;
	extName=fso.GetExtensionName(fileName);
	if (extName=="asp") continue;
	Response.Write("<tr>");
	Response.Write("<td><a href=\"" + fileName + "\">" + fileName + "</a></td>");
	Response.Write("</tr>\n");
}
%>
</table>

<hr>

<script language="JavaScript">
document.write("Last updated on " + document.lastModified + ".")
</script>

</font>
</body>
</html>
