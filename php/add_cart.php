<?php
error_reporting(0);
include_once("dbconnect.php");
$bookid = $_POST['bookid'];
$userid = $_POST['userid'];
$quantity = $_POST['quantity'];
$price = $_POST['price'];
$bookname = $_POST['bookname'];
$status = "not complete";

$sqlsel = "SELECT * FROM BOOKS WHERE BOOKID = '$bookid'";
$result = $conn->query($sqlsel);
if ($result->num_rows > 0) {
    while ($row = $result ->fetch_assoc()){
        $qavail = $row["QUANTITY"];
    }
    $bal = $qavail - $quantity; 
}

$sqlsel = "SELECT * FROM BOOKS WHERE BOOKID = '$bookid'";
$result = $conn->query($sqlsel);
if ($result->num_rows > 0) {
    while ($row = $result ->fetch_assoc()){
        $qavail = $row["QUANTITY"];
    }
    $bal = $qavail - $quantity; 
    if ($bal>0){
        $sqlupdate = "UPDATE BOOKS SET QUANTITY = '$bal' WHERE BOOKID = '$bookid'";
        $conn->query($sqlupdate);
        $sqlinsert = "INSERT INTO CART(BOOKID,USERID,QUANTITY,PRICE,BOOKNAME,STATUS) VALUES ('$bookid','$userid','$quantity','$price','$bookname','$status')";
        if ($conn->query($sqlinsert) === TRUE){
            echo $bal."success";
        }else {
            echo "failed";
        }
    }
}else{
    echo "failed";
}



?>