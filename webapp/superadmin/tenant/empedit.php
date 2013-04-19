<?php
session_start();
//初始化session

include ('../conn.php');
//包含数据库配置文件

if (isset($_SESSION['UserName'])){
//验证session是否已经被设置，如果没有设置则限制访问，如果已经登陆则继续执行下文

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="../styles/layout.css" rel="stylesheet" type="text/css" />
	<link href="../themes/blue/styles.css" rel="stylesheet" type="text/css" />
</head>
<script language="javascript">
		function check(){
			if(empedit.sno.value==""){
				
                                alert("学号不能为空！");
				return false;
			}
			if(empedit.sname.value==""){
				
                                alert("姓名不能为空！");
				return false;
			}
                        if(empedit.sdept.value==""){
				
                                alert("学院不能为空！");
				return false;
			}
                        if(empedit.shome.value==""){
				
                                alert("住址不能为空！");
				return false;
			}
		}
</script>
<body>
	<!-- 右侧主页面上部导航条开始 -->
    <div id="breadcrumb">
    	<ul>	
        	<li><img src="../img/icons/icon_breadcrumb.png" alt="Location" /></li>
        	<li><strong>Location:</strong></li>
            <li><a href="../right_list.php" title="返回首页">欢迎</a></li>
            <li>/</li>
	        <li class="current">修改学生信息</li>
        </ul>
    </div>
    <!-- 右侧主页面上部导航条结束 -->

<?php
$sid=$_GET["sid"];
if(!isset($_POST["submit1"])){
	$sql="select * from Staff where Sid=$sid";
	$result=mysql_query($sql);
	$row=mysql_fetch_array($result);
        $sq="select * from DS where Sid=$sid ";
	$rr=mysql_query($sq);
	if($r1=mysql_fetch_array($rr))
        {
        $dno=$r1["Dno"];
	$sq2="select * from Depart where Dno=$dno";
	$rr2=mysql_query($sq2);
	$r2=mysql_fetch_array($rr2);
        $dname=$r2["Dname"];
        $date=$r1["Workdate"];
        }
        else
        {
        	   $dname='';
    		    $date='';
        }

        
?>

<form method="post" action="" name="empedit"  onsubmit="return check()">
	<table>
		<tr>
			<td>学号</td>
			<td><input type="text" name="sno" value="<?php echo $row["Sno"] ?>"></td>
		</tr>
		<tr>
			<td>姓名</td>
			<td><input type="text" name="sname" value="<?php echo $row["Sname"] ?>"></td>
		</tr>
		<tr>
			<td>性别</td>
			<td><label>
                          <input type="radio" name="sex" <?php if($row["Ssex"]=='男'){?> checked="checked"<?php } ?> id="sex1" value="1" />
      男
      <input type="radio" name="sex" <?php if($row["Ssex"]=='女'){?> checked="checked"<?php } ?>   id="sex2" value="0" />
      女
                  </label></td>
		</tr>
		<tr>
			<td>学院</td>
			<td><input type="text" name="sdept" value="<?php echo $row["Sdept"] ?>"></td>
		</tr>
		<tr>
			<td>住址</td>
			<td><input type="text" name="shome" value="<?php echo $row["Shome"] ?>"></td>
		</tr>
		<tr>
			<td>部门</td>
			<td>
                         <select name="dno">
              
                <?php $sql2="select * from Depart";
                $result2=mysql_query($sql2);
                while($row2=mysql_fetch_array($result2))
                {
                	$dno1=$row2["Dno"];
                        $dname1=$row2["Dname"];
                        if($dno1==$dno)
                        {
                	echo "<option value=".$dno1." selected=\"selected\">$dname1</option>";
                        }
                        else
                        {
                        echo "<option value=".$dno1.">$dname1</option>";
                        }
                        
                }
                ?>
                </select>
                        
                        </td>
		</tr>
		<tr>
			<td>工作日期</td>
			<td><input type="text" name="date" value="<?php echo $date ?>"></td>
                        
		</tr>
                <tr>
			<td>工资</td>
			<td><input type="text" name="salary" value="<?php echo $r1["Salary"] ?>"></td>
                        <td><input type="hidden" name="sid" value="<?php echo $row["Sid"] ?>"></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="修改" name="submit1"></td>
		</tr>
	</table>
</form>

<?php }?>
<?php
 if (isset($_POST["submit1"])){

 	$sid=$_POST["sid"];
 	$sno=$_POST["sno"];
 	$dno=$_POST["dno"];
 	$sname=$_POST["sname"];
 	$date=$_POST["date"];
        $sdept=$_POST["sdept"];
        $shome=$_POST["shome"];
        $salary=$_POST["salary"];
        if($_POST["sex"]==0)
        {
        	$sex="女";
        }
        else
        {
        	$sex="男";
        }
        
 	//获取输入的值 并作出修改处理
        $sql3="select * from Staff where Sid='$sid'";
         $result3= mysql_query($sql3);
         $row3=mysql_fetch_array($result3);
         $presno=$row3["Sno"];
    
                $sql1="select * from Staff where Sno='$sno' and Sno!='$presno'";
               $result1= mysql_query($sql1) ;
                
                if(mysql_num_rows($result1))
                {
                	echo "<br><br><br><center>学号不得重复！<br><br><br><br></center>";
			echo "<meta http-equiv=\"refresh\" content=\"2;url=empedit.php?sid=".$sid."\">";
                }
        else
        {
        
        
        $strSql="update Staff set Sno=$sno,Sname='$sname',Ssex='$sex',Sdept='$sdept',Shome='$shome'  where Sid=$sid";
		mysql_query($strSql) or die(mysql_error());
        
        
      $sql2="select * from Depart where Sid=$sid";
      $result2=mysql_query($sql2);
       if($row2=mysql_fetch_array($result2)){
       $predno=$row2["Dno"];
         if($predno!=$dno){
          $strSql="update Depart set Sid=0  where Dno=$predno";
		mysql_query($strSql) or die(mysql_error());
                echo "<br><br><br><center>该同学换部门后原部门没部长了，请重新任命部长！<br><br><br><br></center>";
         }
       
       }
 	$sql="select * from DS where Sid=$sid";
	$result=mysql_query($sql);
        
      if(mysql_num_rows($result)){
          
        /*     $sq2="select * from Depart where Dname='$dname'";
	$rr2=mysql_query($sq2);
$r2=mysql_fetch_array($rr2);*/

        
 	$strSql="update DS set Dno=$dno,Workdate='$date',Salary='$salary'  where Sid=$sid";
		mysql_query($strSql) or die(mysql_error());
		echo "<br><br><br><center>您已经成功修改了信息！<br><br><br><br></center>";
		echo	"<meta http-equiv=\"refresh\" content=\"2;url=emplist.php\">";
              }
              else
              {
   

 	$strSql="insert into DS (Sid,Dno,Workdate)"."values('$sid','$dno','$date')";
		mysql_query($strSql) or die(mysql_error());
		echo "<br><br><br><center>您已经成功修改了信息！<br><br><br><br></center>";
		echo	"<meta http-equiv=\"refresh\" content=\"2;url=emplist.php\">";
              }
 }
 }
}else{
	//session验证失败 跳转到错误页面
	session_unset();
	session_destroy();
	mysql_close($conn);
	header("Location:../error.html");
	exit;
}
?>
</body>
</html>


