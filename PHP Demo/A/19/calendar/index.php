<?php
$link = @mysql_connect("localhost", "tad", "12345")  or die("糟糕！無法連上資料庫喔！" . mysql_error());

//流程控制區（判斷使用者要做的動作，去呼叫相關的函數或物件方法）
switch ($_REQUEST['op']) {
case "input_form":
  $main=input_form();
  break;
case "add_event":
  $date=add_event();
  header("location: {$_SERVER['PHP_SELF']}?op=show_event&date={$date}");
  break;
case "show_event":
  $main=show_event($_GET['date']);
  break;
case "modify_event":
  $main=input_form($_GET['sn']);
  break;
case "del_event":
  del_event($_GET['sn']);
  header("location: {$_SERVER['PHP_SELF']}");
  break;
default:
  $main=list_event();
  break;
}

//呈現畫面區（若此動作是需要呈現在畫面上的，那麼統一在此輸出）
echo make_page($main);

//函數區（所有function皆置於此處）

//畫面製作區
function make_page($main){
  $page="
  <html>
  <head>
  <meta http-equiv='content-type' content='text/html; charset=Big5'>
  <link rel='stylesheet' type='text/css' media='screen' href='style.css'>
  <title>我的事件本</title>
  <script language='JavaScript' type='text/JavaScript'>
  <!--
  function delete_data(sn){
    var sure = window.confirm('確定要刪除此資料？');
    if (!sure)	return;
    location.href='{$_SERVER['PHP_SELF']}?op=del_event&sn=' + sn;
  }
  //-->
  </script>
  </head>
  <body background='images/bg.gif'>
  
  <div class='center_block'>
    <img src='images/logo.png' class='logo'>
    <div class='toolbar'>
      <a href='{$_SERVER['PHP_SELF']}?op=input_form'>新增事件</a>
      <a href='{$_SERVER['PHP_SELF']}'>事件總覽</a>
    </div>
    $main
  </div>
  <div class='copyright'>Powered by Tad (tadbook5@gmail.com) (c) 2001-2005</div>
  </body>
  </html>";
  
  return $page;
}

//輸入事件的介面
function input_form($sn=""){
  global $link;
  if(!empty($sn)){
    $sql="select * from calendar where sn='$sn'";
    $result=mysql_db_query("myweb",$sql,$link) or die("無法取事件資料！<br>".$sql);
    $event= mysql_fetch_assoc($result);
    $start_time=$event['start_time'];
    $end_time=$event['end_time'];
    $event_title=$event['event_title'];
    $sn_col="<input type='hidden' name ='sn' value='{$event['sn']}'>";
  }else{
    $event_title="";
    $d=explode("-",$_GET['date']);
    $start_time=(empty($_GET['date']))?date("Y-m-d H:i:s"):$_GET['date'].date(" H:i:s",mktime(8,0,0,$d[1],$d[2],$d[0]));
    $end_time=(empty($_GET['date']))?date("Y-m-d H:i:s"):$_GET['date'].date(" H:i:s",mktime(15,0,0,$d[1],$d[2],$d[0]));
    $sn_col="";
  }
      
  $main="<form action='{$_SERVER['PHP_SELF']}' method='post'>
  事件標題：<input type='text' name ='event_title' value='{$event_title}' class='input'>
  <textarea name='event_content' cols=20 rows=10>{$event['event_content']}</textarea>
  $sn_col
  <br>
  起始時間：<input type='text' name ='start_time' value='{$start_time}'>
  結束時間：<input type='text' name ='end_time' value='{$end_time}'>
  <input type='hidden' name ='op' value='add_event'>
  <input type='submit' value='儲存' class='input_btn'>
  </form>";
  return $main;
}

//新增資料
function add_event(){
  global $link;
  if(!get_magic_quotes_gpc()){
    $_POST['event_content']=addslashes($_POST['event_content']);
  }
  $sql="replace into calendar (sn,start_time,end_time,event_title,event_content) values('{$_POST['sn']}','{$_POST['start_time']}','{$_POST['end_time']}', '{$_POST['event_title']} ', '{$_POST['event_content']} ')";
  mysql_db_query("myweb",$sql,$link) or die("無法寫入事件喔！<br>".$sql);
  return $_POST['date'];
}

//顯示單一頁內容
function show_event($date){
  global $link;
  $sql="select sn,start_time,end_time,event_title,event_content from calendar where start_time like '{$date}%' order by sn";
  $result=mysql_db_query("myweb",$sql,$link) or die("無法取事件資料！<br>".$sql);
  $main="<div class='date'>$date</div>";
  while(list($sn,$start_time,$end_time,$event_title,$event_content) = mysql_fetch_row($result)){
    $event_content=nl2br($event_content);
    $main.="
      <div class='event_title'>$event_title</div>
      <p class='event_content'>      
      {$event_content}
      <div class='time'>$start_time ～ $end_time</div>
      <div class='admin_tool'>|
      <a href='{$_SERVER['PHP_SELF']}?op=modify_event&sn={$sn}'>編輯</a> |
      <a href='javascript:delete_data($sn)'>刪除</a> |
      </div>
    </p>";
  }
  return $main;
}

//刪除一筆資料
function del_event($sn){
  global $link;
  $sql="delete from calendar where sn='$sn'";
  mysql_db_query("myweb",$sql,$link) or die("無法刪除事件喔！<br>".$sql);
}

//秀出事件索引
function list_event(){
  global $link;
  $sql="select DISTINCT left(start_time,10) from calendar order by start_time desc";
  $result=mysql_db_query("myweb",$sql,$link) or die("無法取事件資料！<br>".$sql);
  $main="";
  while(list($start_time) = mysql_fetch_row($result)){
    $day_array[]=$start_time;    
  }
  $main=make_calendar($day_array,$_GET['year'],$_GET['mon']);
  return $main;
}

//製作月曆
function make_calendar($day_array=array(),$year="",$mon=""){
  //抓取本年度
  if(empty($year))$year=date("Y");
  //抓取本月月份
  if(empty($mon))$mon=date("m");
  //今天日期
  $today=date("Y-m-d");
  //先抓取這個月有幾天
  $day_num=date("t",mktime(0,0,0,$mon,1,$year));
  //抓取本月1日是星期幾
  $first_w=date("w",mktime(0,0,0,$mon,1,$year));
  //計算所需列數
  $need_cell=$day_num+$first_w;
  $rows=ceil($need_cell/7);
  
  $pre_year=date("Y",mktime(0,0,0,$mon-1,1,$year));
  $pre_mon=date("m",mktime(0,0,0,$mon-1,1,$year));
  $next_year=date("Y",mktime(0,0,0,$mon+1,1,$year));
  $next_mon=date("m",mktime(0,0,0,$mon+1,1,$year));
  
  $cal_tool="<div class='date'>
  <a href='{$_SERVER['PHP_SELF']}?year={$pre_year}&mon={$pre_mon}'>
  <img src='images/pre.png' border=0></a>
  {$year}年{$mon}月
  <a href='{$_SERVER['PHP_SELF']}?year={$next_year}&mon={$next_mon}'>
  <img src='images/next.png' border=0></a>
  </div>";
  
  $calendar="
  $cal_tool
  <table class='cal'><tr>
  <td class='day_w'>日</td><td class='day_w'>一</td>
  <td class='day_w'>二</td><td class='day_w'>三</td>
  <td class='day_w'>四</td><td class='day_w'>五</td>
  <td class='day_w'>六</td></tr>";

  for($i=0;$i<$rows;$i++){
    $calendar.="<tr>";
    for($j=0;$j<7;$j++){      
      if($j==$first_w and $i==0){
        $show_day="1";
        $date=sprintf ("%04d-%02d-%02d",$year,$mon,$show_day);
        $bgcolor=($today==$date)?"#d5d9ea":"#f8f8f8";
        $pic=get_pic($date,$day_array);
      }elseif($show_day>=$day_num){
        $show_day="";
        $bgcolor="#ffffff";
        $pic="";
      }elseif(!empty($show_day)){
        $show_day++;
        $date=sprintf ("%04d-%02d-%02d",$year,$mon,$show_day);
        $bgcolor=($today==$date)?"#d5d9ea":"#f8f8f8";      
        $pic=get_pic($date,$day_array);
      }       
      
      $calendar.="
      <td class='day' bgcolor='$bgcolor'>
      $show_day
      <div class='day_pic'>$pic</div>
      </td>";
    }
    $calendar.="</tr>";
  }
  $calendar.="</table>";
  return $calendar;
}

//取得圖片
function get_pic($date,$day_array){
  if(is_array($day_array) and in_array($date,$day_array)){
    $pic="<a href='{$_SERVER['PHP_SELF']}?op=show_event&date={$date}'>
    <img src='images/diary.png' border=0>
    </a>
    <a href='{$_SERVER['PHP_SELF']}?op=input_form&date={$date}'>
    <img src='images/write.png' border=0>
    </a>";
  }else{
    $pic="<a href='{$_SERVER['PHP_SELF']}?op=input_form&date={$date}'>
    <img src='images/write.png' border=0>
    </a>";
  }
  return $pic;
}
?>
