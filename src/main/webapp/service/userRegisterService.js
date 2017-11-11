var ToDo = angular.module('ToDo')

ToDo.factory('userRegisterService',function($http,$location){
	var details ={};
	
	abc.loginUser = function(user){
		return $http({
			method :"POST",
			url :'userRegister',
			data : user
		})
	}
	return details;
})