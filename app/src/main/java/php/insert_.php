<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include 'connection.php';
$idUser =$_POST['idUser'];
$taskName =$_POST['name'];
$taskDate =date('Y-m-d', strtotime($_POST['date']));
$priority =$_POST['priority'];
$isChecked = $_POST['isChecked'];
echo "Proceeding with query...";
$query ="INSERT INTO kraTASKS(idTask,idUser,name,date,priority,isChecked) values(NULL, $idUser,'$taskName','$taskDate','$priority','$isChecked') ";

echo "SQL query: $query";
$result =mysqli_query($connection,$query);

if($result){
    echo "insert successful";
}else{
    echo "insert failed";
}
mysqli_close($connection);

?>