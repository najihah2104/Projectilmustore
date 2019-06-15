<?php
$servername = "localhost";
$username   = "id9805165_ilmustoreproject";
$password   = "N4jihahnasir";
$dbname     = "id9805165_users";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>