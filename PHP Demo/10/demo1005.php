<?php
header("Content-type: image/png");
$im = @imagecreatetruecolor(80, 20) or die("無法建立圖片！");
$text_color = imagecolorallocate($im, 255, 255, 255);
imagestring($im, 2, 5, 2, "Hi! I'm Tad", $text_color);
imagepng($im);
imagedestroy($im);
?>
