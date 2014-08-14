<?php
//工具列
function toolbar(){
  if(check_user($_SESSION["id"],$_SESSION["passwd"])){
    $main="
    <div class='toolbar'>
    <a href='{$_SERVER['PHP_SELF']}?op=profile'>帳號設定</a>
    <a href='{$_SERVER['PHP_SELF']}?op=logout'>登出</a>
    </div>";
  }else{
    $main="
    <div class='toolbar'>
    <a href='{$_SERVER['PHP_SELF']}?op=register_form'>註冊</a>
    <a href='{$_SERVER['PHP_SELF']}?op=login_form'>登入</a>
    </div>";
  }
  return $main;
}

//註冊表單
function register_form($the_id=""){
  if(!empty($the_id)){
    $op="modify_profile";
    $readonly="readonly";
    $mem=get_mem_data($the_id);

    foreach($mem as $col=>$val){
      $val=stripslashes($val);
      $mem[$col]=$val;
    }
  }else{
    $op="register";
    $readonly="";
  }
  $main=<<<FORM
  <form action="{$_SERVER['PHP_SELF']}" method="post">
  <table class="input_table">
  <tr>
  <td class="col_title">您的姓名：</td>
  <td class="col"><input type="text" name="reg[name]" value="{$mem['name']}" class="txt"></td>
  </tr>
  <tr>
  <td class="col_title">電子郵件：</td>
  <td class="col"><input type="text" name="reg[email]" value="{$mem['email']}" class="txt"></td>
  </tr>
  <tr>
  <td class="col_title">設定帳號：</td>
  <td class="col"><input type="text" name="reg[id]" value="{$mem['id']}" class="txt" $readonly></td>
  </tr>
  <tr>
  <td class="col_title">設定密碼：</td>
  <td class="col"><input type="password" name="reg[passwd]" class="txt"></td>
  </tr>
  <tr>
  <td class="col_title">確認密碼：</td>
  <td class="col"><input type="password" name="reg[passwd2]" class="txt"></td>
  </tr>
  <td colspan="2" align="center">
  <input type="hidden" name="op" value="{$op}">
  <input type="submit" value="註冊" class="input_btn">
  </td>
  </tr>
  </table>
  </form>
FORM;

  return $main;
}

//註冊
function register($user=array()){
  if(empty($user['id']) or empty($user['passwd']))die("請填好欄位嘛！");
  if($user['passwd']!=$user['passwd2'])die("密碼前後輸入不一致喔！");
  if(!eregi("[_.0-9a-z-]+@([0-9a-z-]+.)+[a-z]{2,3}$",$user['email']))die("Email格式不正確喔！");
  if(eregi("[^a-zA-Z0-9]",$user['id']))die("帳號只能用英文數字喔！");

  //利用輸入的帳號去取得資料
  $mem=get_mem_data($user['id']);
  if(!empty($mem['id']))die("此帳號已有人使用囉！");

  $fp = fopen (_MEM_FILE, "a+") or die("無法開啟 "._MEM_FILE." 檔案");
  //取得目前最大的編號
  while ((list($sn,$name,$email,$id,$passwd) = fgetcsv($fp, 1000))) {
    if(!empty($sn))$i=$sn;
  }
  $new_sn=$i+1;

  if(!get_magic_quotes_gpc()){
    foreach($user as $col=>$val){
      $val=addslashes($val);
      $user[$col]=$val;
    }
  }

  $passwd=md5($user['passwd']);
  $content="{$new_sn},\"{$user['name']}\",\"{$user['email']}\",\"{$user['id']}\",\"{$passwd}\"\n";

  fwrite ($fp, $content,strlen($content));
  fclose($fp);
}

//登入表單
function login_form(){
  $main=<<<FORM
  <form action="{$_SERVER['PHP_SELF']}" method="post">
  <table class="input_table">
  <tr>
  <td class="col_title">帳號：</td>
  <td class="col"><input type="text" name="id" class="txt"></td>
  </tr>
  <tr>
  <td class="col_title">密碼：</td>
  <td class="col"><input type="password" name="passwd" class="txt"></td>
  </tr>
  <td colspan="2" align="center">
  <input type="hidden" name="op" value="login">
  <input type="submit" value="登入" class="input_btn">
  </td>
  </tr>
  </table>
  </form>
FORM;

  return $main;
}

//取得某人的資料
function get_mem_data($the_id="") {
  if(empty($the_id))return;
  $fp = fopen(_MEM_FILE, "r") or die("無法開啟 "._MEM_FILE." 檔案");
  while ((list($sn,$name,$email,$id,$passwd) = fgetcsv($fp, 1000))) {
    if($the_id==$id){
      return array("sn"=>$sn,"name"=>$name,"email"=>$email,"id"=>$id,"passwd"=>$passwd);
    }else{
      continue;
    }
  }
  fclose($fp);
}

//身份確認
function check_user($id="",$passwd="",$md5=false){
  if(empty($id) or empty($passwd))return false ;
  if($md5)$passwd=md5($passwd);
  $user=get_mem_data($id);
  if($user['id']==$id and $user['passwd']==$passwd){
    if(empty($_SESSION["id"])){
      $_SESSION["id"]=$id;
      $_SESSION["passwd"]=$passwd;
      $_SESSION["email"]=$user['email'];
    }
    return true;
  }
  return false;
}

//秀出所有通訊錄內容
function listall(){
  return "通訊錄內容（製作中...）";
}

//登出
function logout(){
	$_SESSION = array();
	session_destroy();
}

//修改註冊資料
function modify_profile($user=array()){
  if(empty($user['id']) or empty($user['passwd']))die("請填好欄位嘛！");
  if($user['passwd']!=$user['passwd2'])die("密碼前後輸入不一致喔！");
  if(!eregi("[_.0-9a-z-]+@([0-9a-z-]+.)+[a-z]{2,3}$",$user['email']))die("Email格式不正確喔！");
  if(eregi("[^a-zA-Z0-9]",$user['id']))die("帳號只能用英文數字喔！");
  if($_SESSION["id"]!=$user['id'])die("這不是您的帳號喔！不能修改哩！");

  $fp = fopen (_MEM_FILE, "r");
  $content="";
  while ((list($sn,$name,$email,$id,$passwd) = fgetcsv($fp, 1000))) {
    if($id==$user['id']){
      if(!get_magic_quotes_gpc()){
        foreach($user as $col=>$val){
          $val=addslashes($val);
          $user[$col]=$val;
        }
      }
      $passwd=md5($user['passwd']);
      $content.="{$sn},\"{$user['name']}\",\"{$user['email']}\",\"{$id}\",\"{$passwd}\"\n";
      $_SESSION['passwd']=$passwd;
      $_SESSION['email']=$user['email'];
    }else{
      $content.="{$sn},\"{$name}\",\"{$email}\",\"{$id}\",\"{$passwd}\"\n";
    }
  }
  fclose($fp);

  $fp = fopen (_MEM_FILE, "w");
  fwrite ($fp, $content,strlen($content));
  fclose($fp);
}
?>
