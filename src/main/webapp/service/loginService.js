/**
 * 
 */

var ToDo = angular.module('ToDo')

ToDo.factory('loginService',function($http,$location){
	var details ={};
	
	details.loginUser = function(user){
		console.log("Inside login service");
		return $http({
			method :"POST",
			url :'login',
			data : user
		})
	}
	return details;
});