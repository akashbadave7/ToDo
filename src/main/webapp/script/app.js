var ToDo = angular.module('ToDo', ['ui.router', 'ngSanitize','ngAnimate', 'ngMaterial'])

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
				templateUrl : 'template/home.html'
			});
			
			$stateProvider.state('fb',{
				url :'/fb',
				controller : 'facebookController'
			});
			
			$stateProvider.state('/ResetEmail',{
				url:'/ResetEmail',
				templateUrl : 'template/ResetEmail.html',
				/*controller : 'resetPasswordController'*/
			});
			
			$urlRouterProvider.otherwise('login');
}]);