<?php
$data="count.txt";
//產生新值
if(file_exists($data)){
	//讀取舊值並加1成為新值
	$fp=fopen($data,"r");
	$old_count=fread($fp,filesize($data));
	$new_count=$old_count+1;
	fclose($fp);
}else{
	$new_count=1;
}
//寫入新值到count.txt中
$fp=fopen($data,"w");
fwrite($fp,$new_count);
fclose($fp);
//圖片生成
header("Content-type: image/png");
$im = @imagecreatefrompng ("counter_bg.png") or die("無法建立圖片！");
$text_color = imagecolorallocate($im, 0, 0, 0);
imagettftext($im, 20, 0, 100, 40, $text_color, "C:\WINDOWS\Fonts\arial.ttf",$new_count);
$web_name=iconv("Big5","UTF-8","TAD 的自製 PHP 圖形計數器");
$web_name_color1 = imagecolorallocate($im, 255, 255, 0);
$web_name_color2 = imagecolorallocate($im, 0, 0, 0);
imagettftext($im, 9, 0, 81, 66, $web_name_color2, "C:\WINDOWS\Fonts\mingliu.ttc",$web_name);
imagettftext($im, 9, 0, 80, 65, $web_name_color1, "C:\WINDOWS\Fonts\mingliu.ttc",$web_name);
imagepng($im);
imagedestroy($im);
?>
