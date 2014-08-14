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
$im = @imagecreatetruecolor(80, 20) or die("無法建立圖片！");
$text_color = imagecolorallocate($im, 255, 255, 255);
imagestring($im, 5, 5, 1,  $new_count, $text_color);
imagepng($im);
imagedestroy($im);
?>
