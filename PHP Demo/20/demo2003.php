<?php
include "demo2001.php";

class shopping_pro extends shopping{  
  //輸出購物清單
  function showlist($title="購物清單"){    
    $total=0;
    $all_num=0;
    $list="<h3>{$title}</h3><hr>";
    foreach($this->products as $things => $num){
      $money= $num * $this->price;
      $list.="<li>「{$things}」×「{$num}」件 = {$money} 元";
      $total+=$money;
      $all_num+=$num;
    }
    
    if($all_num>=8){
      $new_money = $total * 0.8;
      $list.="<hr>由於買了 8 件以上，原價 {$total} 元打八折，優惠價 {$new_money} 元";
    }else{
      $list.="<hr>共計 {$total} 元";
    }
    $list.="<br>結帳日期：{$this->date}";
    return $list;
  }
}
?>
