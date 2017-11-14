/**
 * 
 */
var ToDo = angular.module('ToDo')

ToDo.controller('loginController',function($scope,loginService,$location){
	$scope.loginUser= function(){
		console.log("At the beggining of controller");
		var a = loginService.loginUser($scope.user,$scope.error);
		console.log(a);
			a.then(function(response){
				console.log(response.data.responseMessage);
				localStorage.setItem('token',response.data.responseMessage);
				console.log("login success");
/*				$scope.email= $scope.user.email;
				$scope.name = $scope.user.name;*/
				$location.path('home');
			},function(response){
				if(response.status==409)
					{
						$scope.error=response.data.responseMessage;
					}
				else
					{
						$scope.error=response.data.responseMessage;
					}
			});
	}
});
