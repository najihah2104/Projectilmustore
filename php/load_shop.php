<?php
error_reporting(0);
include_once("dbconnect.php");
$location = $_POST['location'];
if (strcasecmp($location, "All") == 0){
    $sql = "SELECT * FROM SHOP"; 
}else{
    $sql = "SELECT * FROM SHOP WHERE LOCATION = '$location'";
}
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    $response["shop"] = array();
    while ($row = $result ->fetch_assoc()){
        $shoplist = array();
        $shoplist[shopid] = $row["SHOPID"];
        $shoplist[name] = $row["NAME"];
        $shoplist[phone] = $row["PHONE"];
        $shoplist[address] = $row["ADDRESS"];
        $shoplist[location] = $row["LOCATION"];
        array_push($response["shop"], $shoplist);
    }
    echo json_encode($response);
}else{
    echo "nodata";
}
?>