var ToDo = angular.module('ToDo')

ToDo.controller('homeController', function ($scope,fileReader,$location, $timeout, $mdSidenav,noteService,$mdDialog,mdcDateTimeDialog,toastr
							,$filter,$interval,$http) {
   
	$scope.file_changed = function(element) {

        var photofile = element.files[0];
        var reader = new FileReader();
        reader.onload = function(e) {
            $scope.$apply(function() {
                $scope.prev_img = e.target.result;
            });
        };
        reader.readAsDataURL(photofile);
        console.log(photofile.name); 
	};
	
	$scope.pinStatus = false;
	$scope.pinUnpin=function(){
		if ($scope.pinStatus == false) {
			$scope.pinStatus = true;
		} else {
			$scope.pinStatus = false;
		}
	}

/*	$scope.date = new Date();
    $scope.time = new Date();
    $scope.dateTime = new Date();
    $scope.minDate = moment().subtract(1, 'month');
    $scope.maxDate = moment().add(1, 'month');
    $scope.dates = [new Date('2016-11-14T00:00:00'), new Date('2016-11-15T00:00:00'),
      new Date('2016-11-30T00:00:00'), new Date('2016-12-12T00:00:00'), new Date('2016-12-13T00:00:00'),
      new Date('2016-12-31T00:00:00')];	*/

	/*//////////////////////////////=====REMINDER======///////////////////////////// */
	
    $scope.displayDialog = function (note) {
      mdcDateTimeDialog.show({
       /* maxDate: $scope.maxDate,*/
        time: true
      })
        .then(function (date) {
        	console.log(date);
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
	

    

	
	/*//////////////////////////////=====COLOR======///////////////////////////// */

	
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
    
	/*//////////////////////////////=====GET ALL NOTES======///////////////////////////// */

    var getNotes=function(){
    	
    	/*var token = localStorage.getItem('token');*/
    	var url = 'getNotes';
    	
    	var notes=noteService.service(url,'GET',notes);
    	notes.then(function(response){
    		$scope.notes=response.data;
    		console.log(response.data);
    		/*==============REMINDER CHECKER====================*/
    		
    		   $interval(function () {
    		       
    		          for (var i = 0; i < response.data.length; i++) {
    		            if(response.data[i].reminder) {
    		            	var date=new Date(response.data[i].reminder);
    		            	if ($filter('date')(date)== $filter('date')(new Date())) {
    		                toastr.success(response.data[i].body, response.data[i].title);
    		              }
    		            }
    		          }
    		      }, 60000);
    		   
    	},function(response){
    		$scope.error=response.data.responseMessage;
    		$location.path('login');
    	});

    }
    
	/*//////////////////////////////=====ADD NEW NOTE======///////////////////////////// */

    $scope.addNote = function(pinStatus) {
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
			
			$scope.note.pinned=pinStatus;
			$scope.note.color=$scope.color;
		/*	if(date!=null)
				{
				$scope.note.color=date;
				}*/
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
    
	/*//////////////////////////////=====DELETE NOTE FOREVER======///////////////////////////// */

    
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
    
    
	/*//////////////////////////////=====RESTORE NOTE======///////////////////////////// */

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
    
    	/*//////////////////////////////=====DELETE NOTE AND SAVE TO TRASH======///////////////////////////// */

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
    
	/*//////////////////////////////=====PIN NOTE======///////////////////////////// */

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
    
	/*//////////////////////////////=====ARCHIVE NOTE======///////////////////////////// */

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
	

	
	/*//////////////////////////////=====UPDATE NOTE======///////////////////////////// */

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
	
	/*//////////////////////////////=====DELETE REMINDER======///////////////////////////// */

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
				/*User.profileUrl="images/user-icon.svg";*/
				$scope.user=User
			}
			$scope.user=User;
			
		}, function(response) {

		});
		
		}
		/*//////////////////////////////=====UPDATE FUNCTION======///////////////////////////// */

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
		
		
		/*//////////////////////////////=====UPLOAD Image======///////////////////////////// */
		
		$scope.imageSrc = "";

		$scope.$on("fileProgress", function(e, progress) {
			$scope.progress = progress.loaded / progress.total;
		});

		$scope.uploadImage = function(c) {
			$scope.cardClick = c;
			//$scope.imgUpload = '';
			console.log(c + " : " + $scope.imgUpload);
			$('#image-upload').trigger('click');
		}
		
		$scope.type = {};
		$scope.type.image = '';

		$scope.$watch('imgUpload', function updateCardImage(oldImg,
				newImage) {
			console.log($scope.cardClick);
			console.log($scope.imgUpload);
			console.log('hello');
		});
		/*$scope.$watch('imageSrc', function(newimg, oldimg) {
			console.log("hello watcher");
			if ($scope.imageSrc != '') {
				if ($scope.type === 'input') {
					$scope.addimg = $scope.imageSrc;
				} 
				else if($scope.type === 'user'){
					$scope.User.profile=$scope.imageSrc;
					$scope.changeProfile($scope.User);
				}
				else {
					console.log("hello");
					$scope.type.image = $scope.imageSrc;
					$scope.updateNote($scope.type);
				}
			}

		});*/

		
		/*//////////////////////////////=====Make a Copy of  note======///////////////////////////// */
		
		$scope.makeCopy=function(note){
			
			note.noteId=null;
			note.archive=false;
			note.pinned=false;
			note.reminder=null;
			var url='addNote';
			var a = noteService.service(url,'POST',note);
			a.then(function(response) {
			$scope.displayDiv=false;
			
			getNotes();

			}, function(response) {

			getNotes();

			$scope.error = response.data.message;

			});

				
				
		}
		
		
		
    getNotes();
    getUser();
    
    
  
    
});