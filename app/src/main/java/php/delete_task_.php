<?php

include 'connection.php';

$idTask = $_POST["idTask"];

$query = "DELETE FROM kraTASKS WHERE idTask = ?";

//Preparamos el statement para evitar inyecciones
$stmt = mysqli_prepare($connection, $query);
mysqli_stmt_bind_param($stmt, "i", $idTask);

$result = mysqli_stmt_execute($stmt);

if ($result) {
    echo "success";
} else {
    echo "fail";
}

mysqli_stmt_close($stmt);
mysqli_close($connection);

?>
