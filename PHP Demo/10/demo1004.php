<?php
echo "目前人數：".my_counter();

function my_counter(){
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

	$pic="";
	//開始從$new_count擷取出每一個數字
	for($i=0;$i<strlen($new_count);$i++){
		$img_num=substr($new_count,$i,1);
		$pic.="<img src='{$img_num}.png' border=0>";
	}

	return $pic;
}
?>
