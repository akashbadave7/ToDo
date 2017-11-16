
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script
	src="bower_components/angular/angular.js"></script>

<script type="text/javascript"
	src="bower_components/angular-ui-router/release/angular-ui-router.js"></script>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
 
<script src="bower_components/angular-sanitize/angular-sanitize.js"></script>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">


	  <link rel="stylesheet" href="bower_components/angular-material/angular-material.min.css">
      <script src = "bower_components/angular-animate/angular-animate.min.js"></script>
      <script src = "bower_components/angular-aria/angular-aria.min.js"></script>
      <script src = "bower_components/angular-messages/angular-messages.min.js"></script>
      <script src="bower_components/angular-material/angular-material.min.js"></script> 

 <!-- CSS -->
<!-- <link rel="stylesheet" type="text/css" href="css/signup.css"> -->
<link rel="stylesheet" type="text/css" href="css/login.css">

<!-- JAVASCRIPT -->
<script type="text/javascript" src="script/app.js"></script>
<script type="text/javascript" src="script/login.js"></script>

<script type="text/javascript" src="script/formInput.js"></script>


<!-- <script type="text/javascript" src="script/signUp.js"></script> -->


 <!-- CONTROLLERS -->
<script src="controller/loginController.js"></script>
<script src="controller/registerController.js"></script>


 <!-- SERVICES -->
<script type="text/javascript" src="service/loginService.js"></script>
<script type="text/javascript" src="service/userRegisterService.js"></script>

<!-- DIRECTIVE -->


</head>



<body ng-app="ToDo"">
	<div ui-view></div>
</body>
</html>