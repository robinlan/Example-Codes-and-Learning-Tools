<?php
$link = @mysql_connect("localhost", "tad", "12345") or die("糟糕！無法連上資料庫喔！" . mysql_error());

//插入一筆資料為：「在2001/1/1這一天，發給tim薪水1500元」
$sql="insert into money (name,salary,date) values('tim','1500','2001/1/1')";
mysql_db_query("myweb",$sql,$link);

//同時插入如下資料：「在2001/1/15這一天，發給bee薪水1500元」、「在2001/2/1這一天，發給frog薪水5000元」、「在2001/3/1這一天，發給apple薪水2000元」
$sql="insert into money (name,salary,date) values('bee','1500',2001/1/15') , ('frog','5000','2001/2/1') , ('apple','2000','2001/3/1')";
mysql_db_query("myweb",$sql,$link);

//讀出frog的薪水有多少？
$sql="select salary from money where name='frog'";
$result=mysql_db_query("myweb",$sql,$link);
while(list($salary)=mysql_fetch_row($result)){
  echo "frog的薪水有{$salary}<br>";
}

//列出每個人薪水最高一次的日期以及薪水？ 
$sql="select name,date,max(salary) from money group by name";
$result=mysql_db_query("myweb",$sql,$link);
while(list($name,$date,$salary)=mysql_fetch_row($result)){
  echo "{$name}的薪水最高是{$date}的{$salary}<br>";
}

//以日期排序（由遠至近）依序列出日期、姓名、薪水的資料
$sql="select date,name,salary from money order by date";
$result=mysql_db_query("myweb",$sql,$link);
while(list($date,$name,$salary)=mysql_fetch_row($result)){
  echo "{$date}{$name}的薪水是{$salary}<br>";
}

//將薪水由高至低排列，依序列出姓名、薪水，並僅列出四筆資料
$sql="select name,salary from money order by salary desc limit 0,4";
$result=mysql_db_query("myweb",$sql,$link);
while(list($name,$salary)=mysql_fetch_row($result)){
  echo "{$name}的薪水是{$salary}<br>";
}


//更新第5筆資料，並將薪資改為2000，日期改為2001/8/15
$sql="update money set salary='2000',date='2001/8/15' where serial=5";
mysql_db_query("myweb",$sql,$link);

//刪除姓名為tad，且薪水低於2000的資料資料
$sql="delete from money where name='tad' and salary<2000";
mysql_db_query("myweb",$sql,$link);

mysql_close($link);
?> 
