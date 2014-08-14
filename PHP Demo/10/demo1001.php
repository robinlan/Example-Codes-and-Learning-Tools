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

	return $new_count;
}
?>
