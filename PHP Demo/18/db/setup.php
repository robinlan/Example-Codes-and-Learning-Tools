<?php
define("_PIC_DIR","data/pic");
define("_ROOT","tad");

$link = @mysql_connect("localhost", "tad", "12345")  or die("糟糕！無法連上資料庫喔！" . mysql_error());
?>
