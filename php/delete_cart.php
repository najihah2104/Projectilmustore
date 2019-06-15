<?php
error_reporting(0);
include_once("dbconnect.php");
$userid = $_POST['userid'];
$bookid = $_POST['bookid'];
    $sqldelete = "DELETE FROM CART WHERE USERID = '$userid' AND BOOKID='$bookid'";
    if ($conn->query($sqldelete) === TRUE){
       echo "success";
    }else {
        echo "failed";
    }
?>