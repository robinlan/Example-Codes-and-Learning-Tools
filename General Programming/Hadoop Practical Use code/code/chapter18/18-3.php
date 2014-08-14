#!/usr/bin/php
<?php

$word2count = array();

//标准输入STDIN (standard input)
while (($line = fgets(STDIN)) !== false) {
   // 移除小写与空格
   $line = strtolower(trim($line));
   // 切词
   $words = preg_split('/\W/', $line, 0, PREG_SPLIT_NO_EMPTY);
   // 将字+1
   foreach ($words as $word) {
       $word2count[$word] += 1;
   }
}

// 结果果写到 STDOUT (standard output)
foreach ($word2count as $word => $count) {
   echo $word, chr(9), $count, PHP_EOL;
}
?>