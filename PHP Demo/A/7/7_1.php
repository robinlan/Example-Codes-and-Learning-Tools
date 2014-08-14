<?php
if($sex=="boy"){
  if($age < 18){
    echo "謝絕未滿十八歲的毛頭小子";
  }elseif($age > 40){
    echo "您超過有效期限了！";
  }else{
    if($status=="重病" or $status=="進修中" or $status=="已退伍"){
      echo "不用當兵真好";
    }else{
      echo "準備為國效命吧！";
    }
  }
}elseif($sex=="girl"){
  echo "女生不能看喔！";
}
?>
