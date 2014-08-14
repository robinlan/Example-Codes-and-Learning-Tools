<?php
$link = @mysql_connect("localhost", "tad", "12345") or die("糟糕！無法連上資料庫喔！" . mysql_error());

$sql="select * from practice";
$result=mysql_db_query("myweb",$sql,$link);
while(list($num,$name,$sex,$birthday,$salary)=mysql_fetch_row($result)){
  echo "{$name}是{$sex}生，生日是{$birthday}<br>";
}
mysql_close($link);
?> 
