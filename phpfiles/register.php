<?php
$firstname = $_POST['firstname'];
$lastname = $_POST['lastname'];
$username = $_POST['username'];
$email = $_POST['email'];
$password = $_POST['password'];
$password1 = $_POST['password1'];
$response = array();
//Check if all fieds are given
if (empty($firstname) || empty($lastname) || empty($username) || empty($email) || empty($password)) {
    $response['success'] = "0";
    $response['message'] = "Some fields are empty. Please try again!";
    echo json_encode($response);
    die;
}
//Check if password match
if ($password !== $password1) {
    $response['success'] = "0";
    $response['message'] = "Password mistmatch. Please try again!";
    echo json_encode($response);
    die();
}
//Check if email is a valid one
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    $response['success'] = "0";
    $response['message'] = "Invalid email. Please try again!";
    echo json_encode($response);
    die();
}
//Check if email exists
if (checkEmail($email)) {
    $response['success'] = "0";
    $response['message'] = "That email is registered. Please try again!";
    echo json_encode($response);
    die();
}

//Check if email exists
if (checkUsername($username)) {
    $response['success'] = "0";
    $response['message'] = "That username is registered. Please try again!";
    echo json_encode($response);
    die();
}
$userdetails = array(
    'firstname' => $firstname,
    'lastname' => $lastname,
    'username' => $username,
    'email' => $email,
    'password' => md5($password),
);
//Insert the user into the database
if (registerUser($userdetails)) {
    $response['success'] = "1";
    $response['message'] = "User registered successfully!";
    echo json_encode($response);
} else {
    $response['success'] = "0";
    $response['message'] = "User registration failed. Please try again!";
    echo json_encode($response);
}
function registerUser($userdetails) {
    require './db-connect.php';
    $query = "INSERT INTO users (firstname, lastname, username, email, password) VALUES "
            . "(:firstname, :lastname, :username, :email, :password)";
    $stmt = $pdo->prepare($query);
    return $stmt->execute($userdetails);
}
function checkEmail($value) {
    require './db-connect.php';
    $stmt = $pdo->prepare("SELECT * FROM users WHERE email = ? ");
    $stmt->execute([$value]);
    $array = $stmt->fetch(PDO::FETCH_ASSOC);
    $stmt = null;
    return !empty($array);
}
function checkUsername($value) {
    require './db-connect.php';
    $stmt = $pdo->prepare("SELECT * FROM users WHERE username = ? ");
    $stmt->execute([$value]);
    $array = $stmt->fetch(PDO::FETCH_ASSOC);
    $stmt = null;
    return !empty($array);
}