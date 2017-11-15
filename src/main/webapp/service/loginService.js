/**
 * 
 */

var ToDo = angular.module('ToDo')

ToDo.factory('loginService',function($http,$location){
	var login ={};
	console.log("inside login service");
	login.loginUser = function(user){
	
		console.log("Inside login service");
		return $http({
			method :"POST",
			url :'login',
			data : user
		});
	}
	
	login.loginByFacebook = function() {
		return $http({
			method : "GET",
			url : "facebookLogin"
		});
	}

	return login;
});