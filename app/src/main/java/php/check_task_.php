<?php

include 'connection.php';

$idTask =$_POST['idTask'];
$isChecked =$_POST['isChecked'];

$query ="UPDATE kraTASKS SET isChecked = ? WHERE idTask = ?";
$stmt = mysqli_prepare($connection, $query);

if ($stmt) {
    mysqli_stmt_bind_param($stmt, "ii", $isChecked, $idTask);

    $result = mysqli_stmt_execute($stmt);

    if ($result) {
        echo "success";
    } else {
        echo "fail";
    }

    mysqli_stmt_close($stmt);
} else {
    echo "fail";
}

mysqli_close($connection);

?>