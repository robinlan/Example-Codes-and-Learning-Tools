<?php
echo "目前人數：".my_counter();

function my_counter(){
	$data="count.txt";
	//產生新值
	if(file_exists($data)){
		//讀取舊值並加1成為新值
		$old_count=file_get_contents($data);
		$new_count=$old_count+1;
	}else{
		$new_count=1;
	}
	//寫入新值到count.txt中
	file_put_contents($data,$new_count);

	return $new_count;
}
?>
