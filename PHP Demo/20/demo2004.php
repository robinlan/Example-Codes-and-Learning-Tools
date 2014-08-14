<?php
include "demo2003.php";

$ss=new shopping();
$ss->set_price(150);
$ss->buy("上衣",3);
$ss->buy("外套");
$ss->buy("牛仔褲",2);
$ss->buy("外套",2);
echo $ss->showlist("服飾專賣店購物清單");

$ss2=new shopping_pro();
$ss2->set_price(150);
$ss2->buy("上衣",3);
$ss2->buy("外套");
$ss2->buy("牛仔褲",2);
$ss2->buy("外套",2);
echo $ss2->showlist("服飾專賣店購物清單");
?>
