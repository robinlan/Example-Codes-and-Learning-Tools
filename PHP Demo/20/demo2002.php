<?php
include "demo2001.php";

$ss=new shopping();
$ss->buy("¾ó¥ÖÀ¿",2);
$ss->buy("Ã±¦rµ§",2);
$ss->buy("µ§µ©");
echo $ss->showlist();

$ss2=new shopping();
$ss2->set_price(150);
$ss2->buy("¤W¦ç",3);
$ss2->buy("¥~®M");
$ss2->buy("¤û¥J¿Ç",2);
$ss2->buy("¥~®M",2);
echo $ss2->showlist("ªA¹¢±M½æ©±ÁÊª«²M³æ");
?>
