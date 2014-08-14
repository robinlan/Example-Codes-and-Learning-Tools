<?php
session_start();
if($_GET['op']=="big"){
  $_SESSION['fontsize']++;
}elseif($_GET['op']=="small"){
  $_SESSION['fontsize']--;
}elseif(empty($_SESSION['fontsize'])){
  $_SESSION['fontsize']=16;
}

if(!empty($_POST['content']))$_SESSION['content']=$_POST['content'];
?>
<html>
  <head>
  <title>字型大小</title>
  </head>
  <body style='font-size:<?php echo $_SESSION['fontsize'];?>px'>
  <?php echo $_SESSION['content']?>
  <form action='fontsize2.php' method='post'>
  <textarea name="content" rows="5" cols="40"></textarea><br>
  <input type='submit' value='送出'>
  </form>
  
  目前的字型大小為 <?php echo $_SESSION['fontsize'];?>px  
  |<a href='fontsize2.php?op=big'>調大</a>
  |<a href='fontsize2.php?op=small'>縮小</a>|
  </body>
</html>
