var ToDo = angular.module('ToDo', ['ui.router', 'ngSanitize','ngAnimate', 'ngMaterial','tb-color-picker','ngMaterialDatePicker','toastr'])


ToDo.config(['$stateProvider','$urlRouterProvider',
		function($stateProvider,$urlRouterProvider){
			$stateProvider.state('register',{
				url:'/register',
				templateUrl:'template/signup.html',
				controller:'registerController'
			})
			
			.state('login', {
				url : '/login',
				templateUrl : 'template/login.html',
				controller : 'loginController'
			})
			
			.state('home', {
				url : '/home',
				templateUrl : 'template/home.html',
				controller: 'homeController'
			})
		
			.state('fb',{
				url :'/fb',
				controller : 'facebookController'
			})
			
			.state('ResetEmail',{
				url:'/ResetEmail',
				templateUrl : 'template/ResetEmail.html',
				/*controller : 'resetPasswordController'*/
			})
			
			.state('trash',{
				url : '/trash',
				templateUrl : 'template/trash.html',
				controller : 'homeController'
			})
			
			.state('archive',{
				url : '/archive',
				templateUrl : 'template/archive.html',
				controller : 'homeController'
			})
			
			.state('dummy', {
				url : '/dummy',
				templateUrl : 'template/dummypage.html',
				controller : 'dummyController'
			});
			
			$urlRouterProvider.otherwise('login');
}]);