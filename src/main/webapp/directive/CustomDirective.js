var ToDo = angular.module("ToDo");

ToDo.directive("toolBar",function(){
	return {
		templateUrl :'template/NavBar.html' 
	};
});


ToDo.directive("sideBar",function(){
	return {
		templateUrl :'template/sideBar.html' 
	};
});