<?php
echo today();
function today(){
  $cw=array("日","一","二","三","四","五","六");
  $y=date("Y")-1911;
  $w=date("w");
  $date=date("m月d日");
  return "民國{$y}年{$date}星期{$cw[$w]}";
}
?>
