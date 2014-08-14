<?php
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

echo "已將訂單寄出！您也可以到 {$_POST['email']} 收取確認訂單通知，謝謝您的光臨！";
?>
