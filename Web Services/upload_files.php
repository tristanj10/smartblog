<?php
$target_path = "./uploads/";
$target_path = $target_path . basename($_FILES['uploadedfile']['name']);

error_log("Upload File >>" . $target_path . $_FILES['error'] . " \r\n", 3,
    "Log.log");

error_log("Upload File >>" . basename($_FILES['uploadedfile']['name']) . " \r\n",
    3, "Log.log");

if (move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) {
    
} else {
}
?>