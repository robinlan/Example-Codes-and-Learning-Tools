<?php
$num=array(1,2,3,4,5,6,7,8,9);
foreach($num as $i){
  foreach($num as $j){
    $ans=$i*$j;
    echo "[{$i} กั {$j} = {$ans}] ";
  }
  echo "<br>";
}
?>
