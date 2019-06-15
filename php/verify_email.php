<?php
    //ini_set( 'display_errors', 1 );
    error_reporting(0);
    include_once("dbconnect.php");
    $email = $_POST['email'];
    $sql = "SELECT * FROM USER WHERE EMAIL = '$email'";
    $result = $conn->query($sql);
    if ($result->num_rows > 0) {
         while ($row = $result ->fetch_assoc()){
             $ran = $row["PASSWORD"];
         }
        $from = "projectilmu2@gmail.com";
        $to = $email;
        $subject = "From Ilmu Store. Reset your password";
        $message = "Use the following link to reset your password :"."\n https://projectilmustore.000webhostapp.com/member/reset_password.php?email=".$email."&key=".$ran;
        $headers = "From:" . $from;
        mail($email,$subject,$message, $headers);
        echo "success";
    }else{
        echo "failed";
    }
    
    
?>
