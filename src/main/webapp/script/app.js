var ToDo = angular.module('ToDo', ['ui.router', 'ngSanitize'])

ToDo.config(['$stateProvider','$urlRouterProvider',
		function($stateProvider,$urlRouterProvider){
			$stateProvider.state('register',{
				url:'/register',
				templateUrl:'template/signup.html',
				controller:'registerController'
			});
			
			$stateProvider.state('login', {
				url : '/login',
				templateUrl : 'template/login.html',
				controller : 'loginController'
			});
			
			$stateProvider.state('home', {
				url : '/home',
				templateUrl : 'template/home.html',
			});
			
			$urlRouterProvider.otherwise('login');
}])