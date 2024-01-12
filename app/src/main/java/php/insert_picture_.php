<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

include 'connection.php';
$email = $_POST['email'];
$picture = $_POST['picture'];

$query = "UPDATE kraUSERS SET picture = ? WHERE email = ? ";
$stmt = mysqli_prepare($connection, $query);

if ($stmt) {
    mysqli_stmt_bind_param($stmt, "ss", $picture, $email); // Change "ii" to "ss"

    $result = mysqli_stmt_execute($stmt);

    if ($result) {
        echo "success";
    } else {
        echo "fail";
    }

    mysqli_stmt_close($stmt);
} else {
    echo "fail: " . mysqli_error($connection);
}

mysqli_close($connection);
?>
