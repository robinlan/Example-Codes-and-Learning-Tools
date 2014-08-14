<?php
class shopping{
  var $products;
  var $price=10;
  var $date;
  
  //建構函數
  function shopping(){
    $this->date=date("Y年m月d日H時i分s秒");
  }
  
  //買東西
  function buy($thing="",$num=1){
    $this->products[$thing]+=$num;
  }
  
  //重設價錢
  function set_price($new_price=""){
    $this->price=$new_price;
  }
  
  //輸出購物清單
  function showlist($title="購物清單"){    
    $total=0;
    $list="<h3>{$title}</h3><hr>";
    foreach($this->products as $things => $num){
      $money= $num * $this->price;
      $list.="<li>「{$things}」×「{$num}」件 = {$money} 元";
      $total+=$money;
    }
    $list.="<hr>共計 {$total} 元<br>結帳日期：{$this->date}";
    return $list;
  }
}
?>
