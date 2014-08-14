<?php
$link = @mysql_connect("localhost", "tad", "12345") or die("糟糕！無法連上資料庫喔！" . mysql_error());

$sql="select * from practice";
$result=mysql_db_query("myweb",$sql,$link);
while($data=mysql_fetch_array($result)){
  echo "{$data['name']}是{$data['sex']}生，生日是{$data['birthday']}<br>";
}
mysql_close($link);
?> 
