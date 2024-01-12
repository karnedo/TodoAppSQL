<?php
include 'connection.php';

$email =$_POST['email'];
$result= array();
$result['picture'] =array();
$query ="SELECT picture FROM kraUSERS WHERE email LIKE '$email' ";
$response = mysqli_query($connection,$query);
while($row = mysqli_fetch_array($response))
{
    $index['picture'] =$row['0'];
    array_push($result['picture'], $index);
}
$result["success"]="1";
echo json_encode($result);

?>