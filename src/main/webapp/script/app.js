var ToDo = angular.module('ToDo', ['ui.router', 'ngSanitize'])

ToDo.config(['$stateProvider','$urlRouterProvider',
		function($stateProvider,$urlRouterProvider){
			$stateProvider.state('register',{
				url:'/register',
				templateUrl:'template/signup.jsp',
				controller:'registerController'
			});
			
			$stateProvider.state('login', {
				url : '/login',
				templateUrl : 'template/login.jsp',
				controller : 'loginController'
			});
			
			$urlRouterProvider.otherwise('login');
}])