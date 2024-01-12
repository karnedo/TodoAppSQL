<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

include 'connection.php';

$email =$_POST['email'];
$password =$_POST['password'];

$result= array();
$result['kraUSERS'] =array();
$query ="SELECT * FROM kraUSERS WHERE email LIKE '$email' AND password LIKE '$password' ";
$response = mysqli_query($connection,$query);

if($row = mysqli_fetch_array($response))
{
    echo "success";
}

?>