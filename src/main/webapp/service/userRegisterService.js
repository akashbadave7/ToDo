var ToDo = angular.module('ToDo')

ToDo.factory('userRegisterService',function($http,$location){
	var details ={};
	
	details.registerUser = function(user){
		return $http({
			method :"POST",
			url :'userRegister',
			data : user
		})
	}
	return details;
})