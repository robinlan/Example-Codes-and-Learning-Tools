<?php
switch($_REQUEST['op']){
case "result":
  $main=result();
  break;
default:
  $main=form();

}

function form(){
  $num1=rand(1,99);
  $num2=rand(1,99);
  $main="<form action='exam.php' method='post'>
  {$num1} × {$num2} = <input type='text' name='user_ans' size=4>
  <input type='hidden' name='n1' value='{$num1}'>
  <input type='hidden' name='n2' value='{$num2}'>
  <input type='hidden' name='op' value='result'>
  <input type='submit' value='看答案'>
  </form>";
  return $main;
}

function result(){
  $ans=$_POST['n1']*$_POST['n2'];
  $main="正確解答：{$_POST['n1']} × {$_POST['n2']} = {$ans}<br>";
  if($_POST['user_ans']==$ans){
    $main.= "您答對了囉！真是厲害！";
  }else{
    $main.="您答錯囉！答案並不是 {$_POST['user_ans']} 喔！";
  }
  return $main;
}
?>

<html>
  <head>
  <title>數學練習</title>
  </head>
  <body>
  <?php echo $main;?>
  </body>
</html>
