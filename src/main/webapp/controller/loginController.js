/**
 * 
 */
var ToDo = angular.module('ToDo')

ToDo.controller('loginController',function($scope,loginService,$location){
	$scope.loginUser= function(){
		console.log("At the beggining of controller");
		var a = loginService.service('POST','login',$scope.user,$scope.error);
		console.log(a);
			a.then(function(response){
				console.log(response.data.responseMessage);
				localStorage.setItem('token',response.data.responseMessage);
				
				console.log("login success");
				$location.path('home');
			},function(response){
				if(response.status==409)
					{
						$scope.error=response.data.responseMessage;
					}
				else
					{	
						console.log("fail");
						$scope.error="Enter valid data";
					}
			});
	}
	
	$scope.forgetPassword=function(){
		console.log($scope.user);
		var message=loginService.service('POST','forgotpassword',$scope.user);
		
		message.then(function(response) {
				//localStorage.setItem('token',response.headers('Authorization'));
			$scope.error=response.data.responseMessage;
		},function(response){
				$scope.error=response.data.responseMessage;
		
			});
	}
	
	$scope.resetpassword = function(){
		var path=$location.path();
		path=path.replace(path.charAt(0),'');
		if($scope.cpassword==$scope.user.password){
		var message=loginService.service('POST',path,$scope.user);
		message.then(function(response) {
				//localStorage.setItem('token',response.headers('Authorization'));
			$scope.error=response.data.message;
			$location.path('/login');
		},function(response){
				$scope.error=response.data.message;
			});
		}else{
			$scope.error='Password does not match';
		}
	}
});
