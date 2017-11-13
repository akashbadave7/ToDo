var ToDo = angular.module('ToDo')

ToDo.controller('registerControlller',function($scope,userRegisterService,$location){
	$scope.registerUser= function(){
		var a= userRegisterService.registerUser($scope.user,$scope.error);
			a.then(function(response){
				console.log("Registrattion succesfull");
				/*localStorage.setItem('token',response.data.responseMessage)*/
				$location.path('/login')
			},function(response){
				/*$scope.error=response.data.responseMessage;*/
				console.log("failed to register");
			});
	}
});