<?php 
$servername = "localhost";
$username = "coderx_mydb";
$password = "czJxHb3";
$dbname = "coderx_cloudmobile";
$con = new mysqli($servername, $username, $password, $dbname);
if ($con->connect_error){
    die("Connection failed : ".$con->connect_error);
}

?>
  
<!DOCTYPE html>
<html>
    <head>
  		<link rel="shortcut icon" href="assets/main_logo.png" type="image/png" />
        <title>Cloud Mobile</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
        <link rel="stylesheet" href="assets/main.css">
        <script src="assets/main.js"></script>
    </head>
    <body id="index_body">
        <img id="illus" class="index_comp" src="assets/illustration.jpg"/>
        <img id="logo" class="index_comp" src="assets/web_logo.png"/>
        <img id="qr_btn" class="index_comp" src="assets/web_qr_btn.png"/>
        <img id="info_bg" class="index_comp" src="assets/info_bg.png"/>
        <div id="app_link" class="index_comp" >
            <a href="assets/mobile cloud 1.0.apk" download>Download our App</a>
        </div>
        <div id="info" class="index_comp" >
            <p id="p1">
                    Cross Platform sharing without any Connection                    
            </p>
            <p id="p2">
                    An invisible storage present in your mobile 
                    accessible from any device. Add any type 
                    of files through our android app into the 
                    storage and access them from another PC 
                    without the need of any wifi connection 
                    or any chord.     
            </p>
        </div>
        <img id="inst_bg" class="index_comp" src="assets/inst_bg.png"/>
        <div id="inst" class="index_comp" >
            <p id="p1">
                Instructions:                   
            </p>
            <p id="p2">
                    <p>1 Open your cloudMobile app in your android phone.</p>
                    <p>1.1 Sign in to the app if you are a new user.</p>
                    <p>2 Scan the QR code displyed on the right.</p>
                    <p>3 Hurray! You can access and upload files of the cloud. </p> 
            </p>
        </div>
        <img id="qr_img" class="index_comp"/>
        <img id="inst_cls" class="index_comp" src="assets/close.png"/>
    </body>
</html>