/**
 * 
 */

var ToDo = angular.module('ToDo')

ToDo.factory('loginService',function($http,$location){
	var login ={};
	
	login.loginUser = function(user){
	
		return $http({
			method :"POST",
			url :'login',
			data : user
		});
	}

	return login;
});