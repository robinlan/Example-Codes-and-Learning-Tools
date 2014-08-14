<?php
echo today(1973,6,16);
function today($year="",$mon="",$day=""){
  $cw=array("日","一","二","三","四","五","六");
  $y=$year-1911;
  $w=date("w",mktime(0,0,0,$mon,$day,$year));
  $date=date("m月d日",mktime(0,0,0,$mon,$day,$year));
  return "民國{$y}年{$date}星期{$cw[$w]}";
}
?>
