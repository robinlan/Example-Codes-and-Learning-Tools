#!/usr/bin/php
<?php

$word2count = array();

// 輸入为 STDIN
while (($line = fgets(STDIN)) !== false) {
    // 移除多余的空白
    $line = trim($line);
    // 每一行的格式为 (字 "tab" 数字) ，记录到($word, $count)
    list($word, $count) = explode(chr(9), $line);
    // 转换格式string -> int
    $count = intval($count);
    // 求总的频数
    if ($count > 0) $word2count[$word] += $count;
}

// 此行不必要，但可让output排列更完整
ksort($word2count);

// 将結果写到 STDOUT (standard output)
foreach ($word2count as $word => $count) {
    echo $word, chr(9), $count, PHP_EOL;
}

?>
