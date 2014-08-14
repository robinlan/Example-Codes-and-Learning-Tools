<html>
  <head>
  <title>MIME查詢</title>
  </head>
  <body>
  <form enctype="multipart/form-data" action="mime.php" method="post">
  <input type="file" name="file"><input type="submit" value="查詢">                        
  </form>
  <?php
  if(!empty($_FILES['file']['name'])){
    echo $_FILES['file']['name']."的檔案MIME類型為：".$_FILES['file']['type'];
  }
  ?>
  </body>
</html>
