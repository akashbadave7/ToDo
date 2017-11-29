var ToDo = angular.module('ToDo')

ToDo.controller('homeController', function ($scope,fileReader,$location, $timeout, $mdSidenav,noteService,$mdDialog,mdcDateTimeDialog,toastr
							,$filter,$interval,$state,Upload, $base64) {
   
	
/*	$scope.file_changed = function(element) {

        var photofile = element.files[0];
        var reader = new FileReader();
        reader.onload = function(e) {
            $scope.$apply(function() {
                $scope.prev_img = e.target.result;
            });
        };
        reader.readAsDataURL(photofile);
        console.log(photofile.name); 
	};*/
	
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
    		                toastr.success(response.data[i].body, response.data[i].title,"Reminder");
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
	/*/////////////////////////////=======GET USER===================/////////////////////*/
		
		var getUser=function(){
		var url='getUser';
		var user = noteService.service(url,'GET');
	
		user.then(function(response) {
			console.log("User ::"+response.data.picUrl);
			var User=response.data;
			console.log("User pic ::"+User.picUrl);
			
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
		
		
		
		$scope.openImageUploader = function(type) {
			$scope.type = type;
			$('#image').trigger('click');
			return false;
		}
		
		
		$scope.stepsModel = [];

		$scope.imageUpload = function(element){
		    var reader = new FileReader();
		    reader.onload = $scope.imageIsLoaded;
		    reader.readAsDataURL(element.files[0]);
		}
	
		$scope.imageIsLoaded = function(e){
		    $scope.$apply(function() {
		        $scope.stepsModel.push(e.target.result);
		        console.log(e.target.result);
		        var imageSrc=e.target.result;
		        $scope.type.image=imageSrc;
		        update($scope.type);
		    });
		};
		
		

		$scope.$on("fileProgress", function(e, progress) {
			$scope.progress = progress.loaded / progress.total;
		});
		
		$scope.type = {};
		$scope.type.image = ''; 
		
		 /*$scope.$watch('file', function () {
			 console.log($scope.file);
		        if ($scope.file != null) {
		        	 $scope.files = [$scope.file]; 
			            console.log($scope.files);
			            console.log("note"+' '+$scope.type.image);
			            $scope.type.image=$scope.file;
			            
		        	if ($scope.type === 'input') {
						$scope.addimg = $scope.files;
					} else{
						console.log("upload:"+imageSrc);
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
		
		/*//////////////////////////////=====LOGOUT ======///////////////////////////// */

		$scope.logout = function() {
			localStorage.removeItem('token');
			$location.path('/login');
		}
		
		/*//////////////////////////////=====CHANGING COLOR ======///////////////////////////// */

		if($state.current.name=='home'){
				$scope.navBarColor="#ffbb33";
				$scope.editable = true;
				$scope.title = "Google Keep";
		} else if($state.current.name=='archive'){
			$scope.navBarColor = "#607D8B";
			$scope.editable = true;
			$scope.title = "Archive";
		} else if($state.current.name=='trash'){
			$scope.navBarColor = "#636363";
			$scope.editable = false;
			$scope.title = "Trash";
		} 
		else if($state.current.name=='reminder'){
			$scope.navBarColor = "#607D8B";
			$scope.editable = true;
			$scope.title = "Reminder";
		} 
		
    getNotes();
    getUser();
    
    
  
    
});