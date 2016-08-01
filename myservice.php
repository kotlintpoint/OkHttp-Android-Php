<?php
	$host="localhost";
	$username="root";
	$password="";
	$db_name="mydatabase";
	
	$con = new mysqli($host, $username, $password, $db_name);
	
	if(mysqli_connect_errno()) 
	{  
		echo "Error: Could not connect to database.";  
		exit;
	}
	
	if(isset($_REQUEST["selectall"]))
	{
		$sql = "select * from contact";
		$result = mysqli_query($con, $sql) or die("Error in Selecting " . mysqli_error($con));

		
		while($row =mysqli_fetch_assoc($result))
		{
			$contactArray['data'][] = $row;
		}
		echo json_encode($contactArray);
	}
	
	if(isset($_REQUEST["insertcontact"]))
	{
		$name=$_REQUEST['name'];
		$number=$_REQUEST['number'];
		
		
		$sql="insert into contact (name, number) values ('$name', '$number')";		
		echo $count=mysqli_query($con, $sql);
	}
	
	if(isset($_REQUEST["deletecontact"]))
	{
		$id=$_REQUEST["id"];
		
		$sql="delete from contact where id=$id";
		echo $count=mysqli_query($con, $sql);
	}
	if(isset($_REQUEST["updatecontact"]))
	{
		$id=$_REQUEST["id"];
		$name=$_REQUEST['name'];
		$number=$_REQUEST['number'];
		
		$sql="update contact set name='$name', number='$number' where id=$id";
		echo $count=mysqli_query($con, $sql);
	}
?>