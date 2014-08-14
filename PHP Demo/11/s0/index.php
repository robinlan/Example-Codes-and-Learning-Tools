<?php
//引入檔案區（看需要引入甚麼檔，都在此先引入）
include "setup.php";
include "function.php";

//流程控制區（判斷使用者要做的動作，去呼叫相關的函數或物件方法）
switch ($_REQUEST['op']) {
case "register_form":
 $main_content = register_form();
	break;
default:
  $main_content = login_form();
  break;
}

//呈現畫面區（若此動作是需要呈現在畫面上的，那麼統一在此輸出）
?>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=Big5">
<link rel="stylesheet" type="text/css" media="screen" href="style.css">
<title>我的通訊錄</title>
</head>
<body background="images/bg.gif">

<div class="center_block">
  <img src="images/title.png" class="logo">
  <?php
  echo toolbar();
  echo $main_content;
  ?>
</div>
<div class="copyright">Powered by Tad (tadbook5@gmail.com) (c) 2001-2005</div>
</body>
</html>
