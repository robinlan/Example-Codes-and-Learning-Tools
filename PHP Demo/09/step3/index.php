<?php
switch($_REQUEST['op']){
	case "process":
	//進行資料處理
	//檢查姓名
	if(empty($_POST['username'])){
		die("沒填姓名呢！這樣不知道您是哪位呢！");
	}

	//檢查Email
	if(empty($_POST['email'])){
		die("親愛的{$_POST['username']}，您沒填Email呢！這樣不能和您聯絡喔！");
	}elseif(!eregi("^[_.0-9a-z-]+@([0-9a-z-]+.)+[a-z]{2,3}$",$_POST['email'])) {
		die("親愛的{$_POST['username']}，您的Email有問題喔！這樣不能和您聯絡喔！");
	}

	//信件內容
	$mail_content = "訂購者：{$_POST['username']}
	訂購者Email：{$_POST['email']}
	訂購物品如下：
	";

	//讀取複選欄位
	foreach($_POST['goods'] as $goods){
		$mail_content .= $goods."\n";
	}

	//抓取當前時間
	$order_time=date("Y年m月d日 H:i:s");
	$mail_content .= "下訂時間：{$order_time}";

	//寄給自己
	@mail("tadbook5@gmail.com","{$_POST['username']} 的訂單",$mail_content) or die("無法寄信給 tadbook5@gmail.com");

	//寄給訂購者
	@mail($_POST['email'],"訂單確認",$mail_content)  or die("無法寄信給 {$_POST['email']}");

	header("location:index.php?op=result&email={$_POST['email']}");
	break;

	case "result":
	//顯示結果
	echo <<<RESULT
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=Big5">
	<link rel="stylesheet" type="text/css" media="screen" href="style.css">
	<title>訂購結果</title>
</head>
<body>
<div class="order_form">
已將訂單寄出！您也可以到 {$_GET['email']} 收取確認訂單通知，謝謝您的光臨！
</div>
</body>
</html>
RESULT;
	break;

	default:
	//顯示表單
	echo <<<FORM
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=Big5">
	<link rel="stylesheet" type="text/css" media="screen" href="style.css">
	<title>簡易訂單</title>
</head>
<body>
<div class="order_form">
	<form action="index.php" method="post">
	<div class="order_col">請輸入姓名：<input type="text" name="username"></div>
	<div class="order_col">請輸入Email：<input type="text" name="email"></div>
	<div class="order_col">
	請選擇您要訂購的物品：<br>
	<input type="checkbox" name="goods[]" value="12吋 PowerBooK">
	12吋 PowerBooK NT 56,900 元<br>
	<input type="checkbox" name="goods[]" value="14吋 PowerBooK">
	14吋 PowerBooK NT 74,900 元<br>
	<input type="checkbox" name="goods[]" value="15吋 PowerBooK">
	15吋 PowerBooK NT 92,900 元<br>
	</div>
	<input type="hidden" name="op" value="process">
	<input type="submit" value="訂購">
	</form>
</div>
</body>
</html>
FORM;
}
?>
