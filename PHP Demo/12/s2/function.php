<?php
//工具列
function toolbar(){
  if(check_user($_SESSION["id"],$_SESSION["passwd"])){
    $main="
    <div class='toolbar'>
    <a href='{$_SERVER['PHP_SELF']}'>通訊錄一覽</a>
		<a href='{$_SERVER['PHP_SELF']}?op=add_form'>新增資料</a>
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


/************ 通訊錄主功能函數 ************/

//用來新增通訊錄的表單
function add_form($sn=""){
  if(!empty($sn)){
    $val_array=get_data($sn);
    if(get_magic_quotes_gpc()){
      foreach($val_array as $k=>$v){
        $val[$k]=stripslashes($v);
      }
    }else{
      $val=$val_array;
    }
    $op="modify";
    $hidden_col="
    <input type='hidden' name='sn' value='{$sn}'>
    <input type='hidden' name='p' value='{$_GET['p']}'>";
  }else{
    $op="add";
  }
  $main=<<<FORM
  <form action="{$_SERVER['PHP_SELF']}" method="post" enctype="multipart/form-data">
  <table class="input_table">
  <tr>
  <td colspan="4" class="col_head_title">編輯通訊資料</td>
  </tr>
  <tr>
  <td class="col_title">好友姓名：</td><td class="col"><input type="text" name="data[name]" value="{$val['name']}" class="txt"></td>
  <td class="col_title">電子郵件：</td><td class="col"><input type="text" name="data[email]" value="{$val['email']}" class="txt"></td>
  </tr>
  <tr>
  <td class="col_title">生日日期：</td><td class="col"><input type="text" name="data[birthday]" value="{$val['birthday']}" class="txt"></td>
  <td class="col_title">個人頭像：</td><td class="col"><input type="file" name="pic"></td>
  </tr>
  <tr>
  <td class="col_title">通訊電話：</td><td class="col"><input type="text" name="data[tel]" value="{$val['tel']}" class="txt"></td>
  <td class="col_title">行動電話：</td><td class="col"><input type="text" name="data[mtel]" value="{$val['mtel']}" class="txt"></td>
  </tr>
  <tr>
  <td class="col_title">聯絡地址：</td><td colspan="3"><input type="text" name="data[addr]" value="{$val['addr']}" class="txt"></td>
  </tr>
  <tr>
  <td class="col_title">其他訊息：</td><td colspan="2"><textarea name="data[note]" rows=3>{$val['note']}</textarea></td>
  <td align="center">
  <input type="hidden" name="op" value="{$op}">
  $hidden_col
  <input type="submit" value="儲存" class="input_btn">
  </td>
  </tr>
  </table>
  </form>
FORM;

  return $main;
}

//新增一筆通訊資料
function add($data_array=array()){
  $fp = fopen (_DATA_FILE, "a+");

  //取得目前最大的編號
  while ((list($sn,$name,$email,$birthday,$pic,$tel,$mtel,$addr,$note) = fgetcsv($fp, 1000))) {
    if(!empty($sn))$i=$sn;
  }
  $new_sn=$i+1;

  //處理上傳的頭像
  if(!empty($_FILES['pic']['name'])){
    $pic_name=up_pic($new_sn);
  }else{
    $pic_name="";
  }

  foreach($data_array as $col=>$val){
    if(!get_magic_quotes_gpc()){
      $val=addslashes($val);
    }
    ${$col}=$val;
  }

  $content.="{$new_sn},\"{$name}\",\"{$email}\",\"{$birthday}\",\"{$pic_name}\",\"{$tel}\",\"{$mtel}\",\"{$addr}\",\"{$note}\"\n";

  fwrite ($fp, $content,strlen($content));
  fclose($fp);
}

//處理上傳圖片
function up_pic($sn=""){
  if(empty($sn))die("沒有檔案編號呢！");
  mk_dir(_PIC_DIR);
  $pic_name="{$sn}_{$_FILES['pic']['name']}";
  $uploadfile=_PIC_DIR."/{$pic_name}";
  move_uploaded_file($_FILES['pic']['tmp_name'], $uploadfile);
  return $pic_name;
}

//建立目錄
function mk_dir($dir=""){
  //若無目錄名稱秀出警告訊息
  if(empty($dir))die("無目錄名稱");
  //若目錄不存在的話建立目錄
  if (!is_dir($dir)) {
    umask(000);
    //若建立失敗秀出警告訊息
    if(!mkdir($dir, 0777))die("無法建立目錄");
  }
}

//秀出出通訊錄
function listall(){
  //處理分頁
  $p=(empty($_GET['p']))?1:$_GET['p'];
  $num=7;
  $i=0;
  $start=($p-1)*$num+1;
  $end=$p*$num;

  //打開資料檔
  if(file_exists(_DATA_FILE)){
    $fp = fopen(_DATA_FILE, "r");
  }else{
    $fp = fopen(_DATA_FILE, "w+");
  }

  $addr_list="";
  //利用fgetcsv()來讀取內容
  while (($csvdata = fgetcsv($fp, 1000))) {

    $i++;
    if($i<$start or $i>$end){
      continue;
    }

    list($sn,$name,$email,$birthday,$pic,$tel,$mtel,$addr,$note)=$csvdata;

    $pic=(empty($pic))?"images/none.gif":_PIC_DIR."/".$pic;
    $name=stripslashes($name);
    $addr=stripslashes($addr);
    $note=nl2br(stripslashes($note));

    if($_SESSION["id"]==_ROOT or $_SESSION["email"]==$email){
      $admin_tool="
      <a href='{$_SERVER['PHP_SELF']}?op=edit&p={$p}&sn={$sn}'>編輯</a><br>
      <a href='javascript:delete_data($sn,$p)'>刪除</a>";
    }else{
      $admin_tool="";
    }

    $addr_list.="<tr class='view'>
    <td rowspan='2' class='func'>
    $admin_tool
    </td>
    <td rowspan='2' valign='top'><img src='{$pic}'></td>
    <td rowspan='2' style='font-size:16px' nowrap align='center'>$name<br>
    <font class='eng'>$birthday</font></td>
    <td class='eng'><a href='mailto:$email'>$email</a></td>
    <td class='eng'>$tel</td>
    <td class='eng'>$mtel</td>
    <td rowspan='2' valign='top'>$note</td>
    </tr>
    <tr>
    <td colspan='3'>$addr</td>
    </tr>";
  }

  $n=ceil($i/$num);
  $page_list="<select onChange=\"if(this.value!='') location.href = '{$_SERVER['PHP_SELF']}?p=' + this.value\">";
  for($a=1;$a<=$n;$a++){
    $selected=($p==$a)?"selected":"";
    $page_list.="<option value='{$a}' $selected>第 $a 頁</option>";
  }
  $page_list.="</select>";

  $next_page=$p+1;
  $previous_page=$p-1;

  $nav="
  <div class='nav'>
  （全部共 $i 筆通訊資料）
  <a href='{$_SERVER['PHP_SELF']}?p={$previous_page}'>上一頁</a>
  $page_list
  <a href='{$_SERVER['PHP_SELF']}?p={$next_page}'>下一頁</a>
  </div>";

  $main=<<<LIST_ALL
  <script language="JavaScript" type="text/JavaScript">
  <!--
  //刪除確認
  function delete_data(sn, p){
    var sure = window.confirm('確定要刪除此資料？');
    if (!sure)	return;
    location.href="{$_SERVER['PHP_SELF']}?op=del&p="+p+"&sn="+sn;
  }
  //-->
  </script>
  $nav
  <table class="list">
  <tr align="center">
  <th nowrap>功能</th>
  <th>頭像</th>
  <th>姓名</th>
  <th>Email</th>
  <th>聯絡電話</th>
  <th>行動電話</th>
  <th>備註</th>
  </tr>
  $addr_list
  </table>
  $nav
LIST_ALL;

  fclose($fp);
  return $main;
}

//取得某一筆通訊資料
function get_data($the_sn=""){
  if(empty($the_sn))return;
  $fp = fopen(_DATA_FILE, "r");
  while ((list($sn,$name,$email,$birthday,$pic,$tel,$mtel,$addr,$note) = fgetcsv($fp, 1000))) {
    if($the_sn==$sn){
      return array("sn"=>$sn,"name"=>$name,"email"=>$email,"birthday"=>$birthday,"pic"=>$pic,"tel"=>$tel,"mtel"=>$mtel,"addr"=>$addr,"note"=>$note);
    }else{
      continue;
    }
  }
  fclose($fp);
}

//變更某筆通訊資料
function modify($the_sn=""){
  if(empty($the_sn))return;

  $fp = fopen (_DATA_FILE, "r");
  $content="";
  while ((list($sn,$name,$email,$birthday,$pic,$tel,$mtel,$addr,$note) = fgetcsv($fp, 1000))) {
    if($sn==$the_sn){
      if($_REQUEST['op']=="del") continue;

      foreach($_POST['data'] as $col=>$val){
        if(!get_magic_quotes_gpc()){
          $val=addslashes($val);
        }
        ${$col}=$val;
      }

      //處理上傳的頭像
      if(!empty($_FILES['pic']['name'])){
        $pic=up_pic($the_sn);
      }
    }
    $content.="{$sn},\"{$name}\",\"{$email}\",\"{$birthday}\",\"{$pic}\",\"{$tel}\",\"{$mtel}\",\"{$addr}\",\"{$note}\"\n";
  }
  fclose($fp);

  $fp = fopen (_DATA_FILE, "w");
  fwrite ($fp, $content,strlen($content));
  fclose($fp);
}
?>
