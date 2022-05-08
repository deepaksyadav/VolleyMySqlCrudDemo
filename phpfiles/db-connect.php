<?php
$host = "localhost";
$dbname = 'android_app';
$user = 'YOUR_USER_NAME';
$password = 'YOUR_PASSWORD';
$pdo = new PDO(
        'mysql:host=' . $host . ';dbname=' . $dbname, $user, $password
);
