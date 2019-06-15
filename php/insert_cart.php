<?php
error_reporting(0);
include_once("dbconnect.php");
$bookid = $_POST['bookid'];
$userid = $_POST['userid'];
$quantity = $_POST['quantity'];
$price = $_POST['bookprice'];
$bookname = $_POST['bookname'];
$shopid = $_POST['shopid'];
$status = "not paid";
    
$sqlsel = "SELECT * FROM BOOKS WHERE BOOKID = '$bookid'";
$result = $conn->query($sqlsel);
if ($result->num_rows > 0) {
    while ($row = $result ->fetch_assoc()){
        $qavail = $row["QUANTITY"];
    }
    $bal = $qavail - $quantity; 
}
if ($bal>0){
    $sqlgetid = "SELECT * FROM CART WHERE USERID = '$userid' AND STATUS='not paid'";
    $result = $conn->query($sqlgetid);
    $sqlupdate = "UPDATE BOOKS SET QUANTITY = '$bal' WHERE BOOKID = '$bookid'";
        $conn->query($sqlupdate);
        
if ($result->num_rows > 0) {
    while ($row = $result ->fetch_assoc()){
        $orderid = $row["ORDERID"];
    }
     $sqlinsert = "INSERT INTO CART(BOOKID,USERID,QUANTITY,PRICE,BOOKNAME,STATUS,SHOPID,ORDERID) VALUES ('$bookid','$userid','$quantity','$price','$bookname','$status','$shopid','$orderid')";
     
    if ($conn->query($sqlinsert) === TRUE){
       echo "success";
    }
}else{
    $orderid = generateRandomString();
   $sqlinsert = "INSERT INTO CART(BOOKID,USERID,QUANTITY,PRICE,BOOKNAME,STATUS,SHOPID,ORDERID) VALUES ('$bookid','$userid','$quantity','$price','$bookname','$status','$shopid','$orderid')";
    if ($conn->query($sqlinsert) === TRUE){
       echo "success";
    }
}
}



function generateRandomString($length = 7) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return date('dmY')."-".$randomString;
}

?>