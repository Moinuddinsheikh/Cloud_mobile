<?php
if (empty($_GET)){
    echo '<script>alert("Invalid authorisation. Please sign in by QR code only.");</script>';
	header("Location: index.php");
	die();    
}
$un = $_GET['username'];
$un2 = str_replace("00","",$un);
$un = pack("H*",$un2);
session_start();
$_SESSION['username'] = $un;
$jsondata = file_get_contents($un.'/data.json');
$jsondata4 = json_decode($jsondata,true);
$files = $jsondata4[$un];
?>
  
<!DOCTYPE html>
<html>
   <head>
  	  <link rel="shortcut icon" href="assets/main_logo.png" type="image/png" />
      <title>User Files</title>
      <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
      <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
      <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
      <script src="assets/main.js"></script>
      <link href="assets/main.css" rel="stylesheet">
   </head>

   <body id="files_body">
      <div class="row">
         <div class="col-lg-8">
            <h3>List of files of <?php echo $un; ?></h3> 
         </div>
         <div class="col-lg-2">
           <form id="uploadform">
             	<input type="hidden" name="action" value="file_upload"/>
             	<input type="hidden" name="username" value="<?php echo $un; ?>"/>
            	<div id="upload" class="btn btn-primary">Upload File</div>
    			<input type="file" id="browse" name="ufile" style="display:none;">
         	</form>
         </div>
         <div class="col-lg-2">
         	<h6 style="margin-top: 7px;"><a style="color: black;" href="logout.php">Logout</a></h6>	
         </div>
      </div><br><br>
      <div class="row">
         <div class="col-lg-12">
           <?php //echo var_dump($files); ?>
            <table id="table" class="table">
               <thead>
                  <tr>
                     <td>S. No.</td>
                     <td>Name</td>
                     <td>Size</td>
                     <td>Type</td>
                     <td></td>
                  </tr>
               </thead>
               <tbody>
                  <?php 
            	$c = 1;
            	foreach ($files as $i){
                    echo '<tr>';
                    echo '<td>'.$c.'</td>';
                    foreach ($i as $j){
                      echo '<td>'.$j.'</td>';
                    }
                    echo '<td><button class="btn btn-default"><a href="'.$un.'/assets/'.$i[0].'" download >Download File</a></button></td>';
                    $c++;
                    echo '</tr>';
                  } ?>
               </tbody>
            </table>
         </div>
      </div>
	<script type="text/javascript">
    	/*setTimeout(function () { 
      		location.reload();
    	}, 60000);	*/
	</script>
   </body>
</html>