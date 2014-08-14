<?php
$link = @mysql_connect("localhost", "tad", "12345") or die("糟糕！無法連上資料庫喔！" . mysql_error());

$sql="insert into practice (name,sex,birthday ,salary) values('phebe','女','1973/03/10',40000)";
if(mysql_db_query("myweb",$sql,$link)){
  echo "資料新增完畢！";
}else{
  echo "無法執行以下語法：<br>" . $sql;
}
mysql_close($link);
?> 
