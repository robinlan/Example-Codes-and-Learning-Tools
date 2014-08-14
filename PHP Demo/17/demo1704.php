<?php
$link = @mysql_connect("localhost", "tad", "12345") or die("糟糕！無法連上資料庫喔！" . mysql_error());

$sql="insert into practice (name,sex,birthday ,salary) values('sherry','女','1973/05/15',50000)";
if(mysql_db_query("myweb",$sql,$link)){
  $new_num=mysql_insert_id($link);
  echo "資料新增完畢！其編號為：{$new_num}";
}else{
  echo "無法執行以下語法：<br>" . $sql;
}
mysql_close($link);
?> 
