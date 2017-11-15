
var ToDo = angular.module('ToDo')

ToDo.controller('facebookController',function($scope,facebookService,$location){
	$scope.fblogin= function(){
		console.log("At the beggining of controller");
		var a = facebookService.fblogin($scope.user,$scope.error);
		
			a.then(function(response){
				console.log(response.data.responseMessage);
				localStorage.setItem('token',response.data.responseMessage);
				console.log("login success");
/*				$scope.email= $scope.user.email;
				$scope.name = $scope.user.name;*/
				$location.path('home');
			},function(response){
				/*if(response.status==409)
					{*/
						$scope.error=response.data.responseMessage;
						$location.path('login');
				/*	}
				else
					{
						$scope.error=response.data.responseMessage;
					}*/
			});
	}
});