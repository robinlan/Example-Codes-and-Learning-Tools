<%@language=jscript%>
<% title="MATLAB程式設計《入門篇》：程式範例" %>
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
fso = Server.CreateObject("Scripting.FileSystemObject");
absTopPath = Server.MapPath(".");
fd = fso.GetFolder(absTopPath);
%>
<table border=1 align=center>
<tr>
<th>各章主題</th>
<%
var subFolderList=new Enumerator(fd.SubFolders);
for (subFolderList.moveFirst(); !subFolderList.atEnd(); subFolderList.moveNext()){
	folderName=subFolderList.item().name;
	Response.Write("<tr>");
	Response.Write("<td><a target=_blank href=\"" + folderName + "\">" + folderName + "</a></td>");
	Response.Write("</tr>\n");
}
%>
</table>

<hr>

<!--
<script language="JavaScript">
document.write("Last updated on " + document.lastModified + ".")
</script>
-->

</font>
</body>
</html>
