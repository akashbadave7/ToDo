var ToDo = angular.module('ToDo')

ToDo.controller('registerControlller',function($scope,userRegisterService,$location){
	$scope.loginUser= function(){
		var a= loginService.loginUser($scope.user,$scope.error);
			a.then(function(response){
				console.log(response.data.responseMessage);
				localStorage.setItem('token',response.data.responseMessage)
				$location.path('/login')
			},function(response){
				$scope.error=response.data.responseMessage;
			});
	}
});