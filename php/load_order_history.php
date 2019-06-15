<?php
error_reporting(0);
include_once("dbconnect.php");
$phone = $_POST['userid'];

$sql = "SELECT * FROM ORDERED WHERE USERID = '$phone'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["history"] = array();
    while ($row = $result ->fetch_assoc()){
        $histlist = array();
        $histlist[orderid] = $row["ORDERID"];
        $histlist[total] = $row["TOTAL"];
        $histlist[date] = $row["DATE"];
        array_push($response["history"], $histlist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>