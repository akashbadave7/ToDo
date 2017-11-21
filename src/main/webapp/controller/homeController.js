var ToDo = angular.module('ToDo')

ToDo.controller('homeController', function ($scope, $timeout, $mdSidenav,noteService) {
   
	
	$scope.toggleLeft = function(){
		$mdSidenav('left').toggle();
	}
   /* 
	$scope.toggleLeft = buildToggler('left');

    function buildToggler(componentId) {
      return function() {
    	  $mdSidenav(componentId).toggle();
    	  if($scope.width=='0px'){
  			$scope.width='200px';
  			$scope.mleft="200px";
  			
  		}else{
  			$scope.width='0px';
  			$scope.mleft="0px";
  			
  		}
       
      };
    }*/
    
    
    var getNotes=function(){
    	
    	var token = localStorage.getItem('token');
    	var notes=noteService.getNotes(token);
    	notes.then(function(response){
    		$scope.notes=response.data;
    	},function(response){
    		$scope.error=response.data.responseMessage;
    		$location.path('login');
    	});
		$scope.notes=notes;

    }
    
    $scope.addNote = function() {
    	$scope.note = {};
    	var token = localStorage.getItem('token');
    	$scope.note.title = document.getElementById("noteTitle").innerHTML;
    	
    	$scope.note.body = document.getElementById("noteBody").innerHTML;
    	
		
		
		
		var notes = noteService.addNote(token, $scope.note);
		
		

		notes.then(function(response) {

			document.getElementById("noteTitle").innerHTML="";
			document.getElementById("noteBody").innerHTML="";
			getNotes();

		}, function(response) {

			getNotes();

			$scope.error = response.data.message;

		});
	}
    /*$scope.displayDiv=false;
    $scope.showDiv=function()
    {
    	$scope.displayDiv=true;
    }
    */

    getNotes();
    
});