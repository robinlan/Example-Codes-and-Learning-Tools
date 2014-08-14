<?php
$link = @mysql_connect("localhost", "tad", "12345") or die("糟糕！無法連上資料庫喔！" . mysql_error());
echo "恭喜資料庫已經連上囉！";
mysql_close($link);
?> 
