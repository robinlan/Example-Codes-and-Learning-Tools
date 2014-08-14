<?php
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

header("location:result.php?email={$_POST['email']}");
?>
