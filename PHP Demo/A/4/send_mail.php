<?php
mail($_POST['mail2who'],$_POST['mail_title'],$_POST['mail_content']);
echo "已經寄出給{$_POST['mail2who']}囉！";
?>
