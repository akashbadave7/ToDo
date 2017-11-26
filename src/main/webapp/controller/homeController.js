var ToDo = angular.module('ToDo')

ToDo.controller('homeController', function ($scope, $timeout, $mdSidenav,noteService,$mdDialog,mdcDateTimeDialog) {
   

/*	$scope.date = new Date();
    $scope.time = new Date();
    $scope.dateTime = new Date();
    $scope.minDate = moment().subtract(1, 'month');
    $scope.maxDate = moment().add(1, 'month');
    $scope.dates = [new Date('2016-11-14T00:00:00'), new Date('2016-11-15T00:00:00'),
      new Date('2016-11-30T00:00:00'), new Date('2016-12-12T00:00:00'), new Date('2016-12-13T00:00:00'),
      new Date('2016-12-31T00:00:00')];	*/

    $scope.displayDialog = function (note) {
      mdcDateTimeDialog.show({
       /* maxDate: $scope.maxDate,*/
        time: true
      })
        .then(function (date) {
          $scope.selectedDateTime = date;
          note.reminder=date;
          console.log('New Date / Time selected:', date);
          
          	var url='update';
  		
	  		var notes = noteService.service(url,'POST',note);
	  		notes.then(function(response) {
	
	  			getNotes();
	
	  		}, function(response) {
	
	  			getNotes();
	
	  			$scope.error = response.data.message;
	
	  		});
        });
    };
	
    

	
	
	
	$scope.colors = [ '#fff', '#ff8a80', '#ffd180', '#ffff8d',
		'#ccff90', '#a7ffeb', '#80d8ff', '#82b1ff',
		'#b388ff', '#f8bbd0', '#d7ccc8', '#cfd8dc' ];
	
	 
	 
	 $scope.noteColor=function(newColor, oldColor)
	 {
		 console.log(newColor);
		 $scope.color = newColor;
	 }
	 
	$scope.colorChanged = function(newColor, oldColor, note) {
        note.color=newColor;
        update(note);
        /*var url='update';
		var notes = noteService.service(url,'POST',note);
		notes.then(function(response) {

			getNotes();

		}, function(response) {

			getNotes();

			$scope.error = response.data.message;

		});*/
    }
	
	
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
    	
		var url='addNote';
		if(document.getElementById("title").innerHTML=="" && document.getElementById("body").innerHTML=="")
			{
				$scope.displayDiv=false;
			}
		else{
			console.log("Note color in adding");
			console.log($scope.color);
			$scope.note.color=$scope.color;
			
			var notes = noteService.service(url,'POST',$scope.note);
			notes.then(function(response) {

			document.getElementById("title").innerHTML="";
			document.getElementById("body").innerHTML="";
			$scope.color='#fff';
			$scope.displayDiv=false;
			
			getNotes();

		}, function(response) {

			getNotes();

			$scope.error = response.data.message;

		});
			
		}
		
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
    		note.trash=false;
    		update(note);
    		/*var url='update';
    		var notes = noteService.service(url,'POST',note);
    		notes.then(function(response) {

    			getNotes();

    		}, function(response) {

    			getNotes();

    			$scope.error = response.data.message;

    		});*/
    	}
    
    $scope.deleteNote = function(note) {

		note.trash=true;
		note.pinned=false;
		update(note);
		/*var url='update';
		
		var notes = noteService.service(url,'POST',note);
		notes.then(function(response) {

			getNotes();

		}, function(response) {

			getNotes();

			$scope.error = response.data.message;

		});*/
	}
    
    $scope.pinned = function(note,pinned) {
		note.pinned=pinned;
		note.archive=false;
		update(note);
		/*var url = 'update';
		var notes = noteService.service(url,'POST',note)
		notes.then(function(response){
			console.log("success")
		},function(response){
			$scope.error=response.data.responseMessage;
		});*/
	}
    
    $scope.archive=function(note,status){
    	console.log("in archive");
    	note.pinned=false;
    	note.archive=status;
    	update(note);
    	/*var url = 'update';
		var notes = noteService.service(url,'POST',note)
		notes.then(function(response){
			console.log("success");
		},function(response){
			$scope.error=response.data.responseMessage;
		});*/
    }
    
	
	$scope.displayDiv=false;
	$scope.show=function(){
		$scope.displayDiv=true;
	}
	

	
	$scope.updateEditedNote = function(note, event) {
	    // Show dialog box for edit a note
		console.log("inside updatenote");
		console.log(note);
	    $mdDialog.show({
	      locals: {
	        dataToPass: note  // Pass the note data into dialog box
	      },
	      templateUrl: 'template/UpdateNote.html',
	      parent: angular.element(document.body),
	      targetEvent: event,
	      clickOutsideToClose: true,
	      controllerAs: 'controller',
	      controller: mdDialogController
	    });
	}
	
	function mdDialogController($scope, $state, dataToPass) {
	      $scope.mdDialogData = dataToPass;

	      // Saving the edited note
	      	$scope.saveUpdatedNote = function() {
	    	/*var url = 'update';*/
	    	
	    	console.log(dataToPass);
	    	
	    	dataToPass.title = document.getElementById("updatedNoteTitle").innerHTML;
	    	
	    	dataToPass.body = document.getElementById("updatedNoteBody").innerHTML;
	    	/*var updatedNoteTitle = document.getElementById("updatedNoteTitle").innerHTML;
	    	
	    	var updatedNoteBody = document.getElementById("updatedNoteBody").innerHTML;*/
	    	
	    	update(dataToPass);
	  		/*var notes = noteService.service(url,'POST',dataToPass)
	  		
	  		notes.then(function(response){
				console.log("success")
			},function(response){
				$scope.error=response.data.responseMessage;
			});*/
	      }
	
	   }
	
		$scope.deleteRemender=function(note){
			
			note.reminder=null;
			update(note);
		}
	
		var getUser=function(){
		var url='getUser';
		var user = noteService.service(url,'GET');
	
		user.then(function(response) {
			var User=response.data;
			if(User.profileUrl==null){
				User.profileUrl="images/profilepic.svg";
				$scope.user=User
			}
			$scope.user=User;
			
		}, function(response) {

		});
		
		}
	
		var update=function(note){
			
			var url='update';
			var notes = noteService.service(url,'POST',note);
			notes.then(function(response) {

				getNotes();

			}, function(response) {

				getNotes();

				$scope.error = response.data.message;

			});
		}
		
		
    getNotes();
    getUser();
    
});