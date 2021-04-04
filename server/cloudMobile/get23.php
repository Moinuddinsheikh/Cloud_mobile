<?php

if (($_FILES['mfile']['name']!="")){
    // Where the file is going to be stored
  		$un = $_POST['username'];
        $target_dir = $un."/assets/";
        $file = $_FILES['mfile']['name'];
        $filename = $file;
        $temp_name = $_FILES['mfile']['tmp_name'];
        $path_filename_ext = $target_dir.$filename;
        if (file_exists($path_filename_ext)) {
            echo "<script>alert('fail');</script>";
        }else{
            move_uploaded_file($temp_name,$path_filename_ext);
            echo "<script>alert('success');</script>";
        }
}

?>