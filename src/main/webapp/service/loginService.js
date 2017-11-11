/**
 * 
 */

var ToDo = angular.module('ToDo')

ToDo.factory('loginService',function($http,$location){
	var details ={};
	
	details.loginUser = function(user){
		return $http({
			method :"POST",
			url :'login',
			data : user
		})
	}
	return details;
});