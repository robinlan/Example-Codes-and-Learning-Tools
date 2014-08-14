<?php
$link = @mysql_connect("localhost", "tad", "12345")  or die("糟糕！無法連上資料庫喔！" . mysql_error());

//函數區（所有function皆置於此處）


//流程控制區（判斷使用者要做的動作，去呼叫相關的函數或物件方法）
switch ($_REQUEST['op']) {
case "input_form":
  $main=input_form();
  break;
case "add_diary":
  $date=add_diary();
  header("location: {$_SERVER['PHP_SELF']}?op=show_diary&date={$date}");
  break;
case "show_diary":
  $main=show_diary($_GET['date']);
  break;
case "modify_diary":
  $main=input_form($_GET['sn']);
  break;
case "del_diary":
  del_diary($_GET['sn']);
  header("location: {$_SERVER['PHP_SELF']}");
  break;
case "mkpdf":
  require('fpdf/mypdf.php');
  mkpdf($_GET['date']);
  break;
default:
  $main=list_diary();
  break;
}

//呈現畫面區（若此動作是需要呈現在畫面上的，那麼統一在此輸出）
echo make_page($main);

//畫面製作區
function make_page($main){
  $page="
  <html>
  <head>
  <meta http-equiv='content-type' content='text/html; charset=Big5'>
  <link rel='stylesheet' type='text/css' media='screen' href='style.css'>
  <title>我的日記本</title>
  <script language='JavaScript' type='text/JavaScript'>
  <!--
  function delete_data(sn){
    var sure = window.confirm('確定要刪除此資料？');
    if (!sure)	return;
    location.href='{$_SERVER['PHP_SELF']}?op=del_diary&sn=' + sn;
  }
  //-->
  </script>
  </head>
  <body background='images/bg.gif'>
  
  <div class='center_block'>
    <img src='images/logo.png' class='logo'>
    <div class='toolbar'>
      <a href='{$_SERVER['PHP_SELF']}?op=input_form'>寫日記</a>
      <a href='{$_SERVER['PHP_SELF']}'>日記總覽</a>
    </div>
    $main
  </div>
  <div class='copyright'>Powered by Tad (tadbook5@gmail.com) (c) 2001-2005</div>
  </body>
  </html>";
  
  return $page;
}

//輸入日記的介面
function input_form($sn=""){
  global $link;
  if(!empty($sn)){
    $sql="select * from diary where sn='$sn'";
    $result=mysql_db_query("myweb",$sql,$link) or die("無法取日記資料！<br>".$sql);
    $diary= mysql_fetch_assoc($result);
    $date=$diary['diary_date'];
    $sn_col="<input type='hidden' name ='sn' value='{$diary['sn']}'>";
  }else{
    $date=(empty($_GET['date']))?date("Y-m-d"):$_GET['date'];
    $sn_col="";
  }
      
  $main="<form action='{$_SERVER['PHP_SELF']}' method='post'>
  <textarea name='diary_content' cols=20 rows=15>{$diary['diary_content']}</textarea>
  $sn_col
  <input type='hidden' name ='date' value='{$date}'>
  <input type='hidden' name ='op' value='add_diary'>
  <input type='submit' value='儲存' class='input_btn'>
  </form>";
  return $main;
}

//新增資料
function add_diary(){
  global $link;
  if(!get_magic_quotes_gpc()){
    $_POST['diary_content']=addslashes($_POST['diary_content']);
  }
  $sql="replace into diary (sn,diary_date,diary_content) values('{$_POST['sn']}','{$_POST['date']}', '{$_POST['diary_content']} ')";
  mysql_db_query("myweb",$sql,$link) or die("無法寫入日記喔！<br>".$sql);
  return $_POST['date'];
}

//顯示單一頁內容
function show_diary($date){
  global $link;
  $sql="select sn,diary_content from diary where diary_date='{$date}' order by sn";
  $result=mysql_db_query("myweb",$sql,$link) or die("無法取日記資料！<br>".$sql);
  $main="<div class='date'>$date</div>";
  while(list($sn,$diary_content) = mysql_fetch_row($result)){
    $diary_content=nl2br($diary_content);
    $main.="<p class='diary_content'>{$diary_content}
      <div class='admin_tool'>|
      <a href='{$_SERVER['PHP_SELF']}?op=mkpdf&date={$date}'>匯出PDF</a> |
      <a href='{$_SERVER['PHP_SELF']}?op=modify_diary&sn={$sn}'>編輯</a> |
      <a href='javascript:delete_data($sn)'>刪除</a> |
      </div>
    </p>";
  }
  return $main;
}

//刪除一筆資料
function del_diary($sn){
  global $link;
  $sql="delete from diary where sn='$sn'";
  mysql_db_query("myweb",$sql,$link) or die("無法刪除日記喔！<br>".$sql);
}

//秀出日記索引
function list_diary(){
  global $link;
  $sql="select DISTINCT diary_date from diary order by diary_date desc";
  $result=mysql_db_query("myweb",$sql,$link) or die("無法取日記資料！<br>".$sql);
  $main="";
  while(list($diary_date) = mysql_fetch_row($result)){
    $day_array[]=$diary_date;    
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
    $pic="<a href='{$_SERVER['PHP_SELF']}?op=show_diary&date={$date}'>
    <img src='images/diary.png' border=0>
    </a>";
  }else{
    $pic="<a href='{$_SERVER['PHP_SELF']}?op=input_form&date={$date}'>
    <img src='images/write.png' border=0>
    </a>";
  }
  return $pic;
}

//製作PDF檔
function mkpdf($date=""){
  global $link;
  //實體化MyPDF，產生$pdf物件
  $pdf=new MyPDF();
  //設定預設畫面
  $pdf->FPDF("P","mm","A5");
  //設定標題
  $pdf->SetTitle("My Diary");
  //設定主題
  $pdf->SetSubject($date);
  //設定作者
  $pdf->SetAuthor("tad");
  //設定邊界
  $pdf->SetMargins(15, 15 ,15);
  //設定自動換頁
  $pdf->SetAutoPageBreak("on","20");
  //設定顯示模式
  $pdf->SetDisplayMode("real");
  //新增一「直書」頁面
  $pdf->AddPage('P');
  //加入中文字型
  $pdf->AddBig5hwFont();  
  //設定文字顏色（黑色）
  $pdf->SetTextColor(0,0,0);
  //設定中文字型、粗體、12級
  $pdf->SetFont('Big5-hw','B',12);
  
  //抓取當天所有日記
  $sql="select sn,diary_content from diary where diary_date='{$date}' order by sn";
  $result=mysql_db_query("myweb",$sql,$link) or die("無法取日記資料！<br>".$sql);
  while(list($sn,$diary_content) = mysql_fetch_row($result)){
    //輸入文字，行距9
    $pdf->Write(9,$diary_content);
    //插入空白行
    $pdf->Ln();
  }

  //輸出PDF
  $pdf->Output("我的日記_{$date}","I");
}
?>
