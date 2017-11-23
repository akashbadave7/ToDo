var ToDo = angular.module('ToDo')

ToDo.controller('homeController', function ($scope, $timeout, $mdSidenav,noteService) {
   

	$scope.toggleLeft = function(){
		$mdSidenav('left').toggle();
	}
   
	var note=function(){
		$location.path('notes');
	}
    
    
    var getNotes=function(){
    	
    	/*var token = localStorage.getItem('token');*/
    	var url = 'getNotes';
    	
    	var notes=noteService.service(url,'GET',notes);
    	notes.then(function(response){
    		$scope.notes=response.data;
    		console.log("$scope.notes::",$scope.notes);
    	},function(response){
    		$scope.error=response.data.responseMessage;
    		$location.path('login');
    	});
		$scope.notes=notes;

    }
    
    $scope.addNote = function() {
    	$scope.note = {};
    	/*var token = localStorage.getItem('token');*/
    	$scope.note.title = document.getElementById("title").innerHTML;
    	
    	$scope.note.body = document.getElementById("body").innerHTML;
		var url='addNote'
		var notes = noteService.service(url,'POST',$scope.note);
		
		

		notes.then(function(response) {

			document.getElementById("title").innerHTML="";
			document.getElementById("body").innerHTML="";
			getNotes();

		}, function(response) {

			getNotes();

			$scope.error = response.data.message;

		});
	}
    
    $scope.deleteNoteForever=function(note){
    	
    	console.log("inside delete forever")
   
    	var url='delete/'+note.noteId;
    	var notes = noteService.service(url,'DELETE',note);
    	notes.then(function(response) {

			getNotes();

		}, function(response) {

			getNotes();

			$scope.error = response.data.message;

		});
    	
    }
    
    	$scope.restoreNote=function(note){
    		note.trash=0;
    		var url='update';
    		var notes = noteService.service(url,'POST',note);
    		notes.then(function(response) {

    			getNotes();

    		}, function(response) {

    			getNotes();

    			$scope.error = response.data.message;

    		});
    	}
    
    $scope.deleteNote = function(note) {

		note.trash=1;
		var url='update';
		var notes = noteService.service(url,'POST',note);
		notes.then(function(response) {

			getNotes();

		}, function(response) {

			getNotes();

			$scope.error = response.data.message;

		});
	}
    
    $scope.displayDiv=false;
	$scope.show=function(){
		$scope.displayDiv=true;
	}

    getNotes();
    
});