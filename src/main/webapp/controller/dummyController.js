var ToDo = angular.module('ToDo');
ToDo.controller('dummyController',function($location,dummyservice){
	var dummyservice=dummyservice.service();
	dummyservice.then(function(response){
		localStorage.setItem('token',response.data.message);
		$location.path('/home');
	},function(response){
		
	});
	dummyservice();
});