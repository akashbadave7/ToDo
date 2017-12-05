var ToDo = angular.module('ToDo')

ToDo.controller('homeController', function ($scope,fileReader,$location, $timeout, $mdSidenav,noteService,$mdDialog,mdcDateTimeDialog,toastr
							,$filter,$interval,$state,Upload, $base64,$q) {
   
	
	
	/*//////////////////////////////=====LIST/GRID VIEW======///////////////////////////// */
	
	
	$scope.view=function(){
		var view = localStorage.getItem('view');
		if(view=='list'){
			$scope.displayView('list');
		}else{
			$scope.displayView('grid');
		}
		
	}
	
	$scope.displayView=function(type){
		
		if(type=='list'){
			$scope.view='90';
			$scope.width='100%';
			$scope.list=false
			$scope.grid=true
			localStorage.setItem('view','list');
		}else{
			$scope.view='30';
			$scope.width='260px';
			$scope.grid=false;
			$scope.list=true;
			localStorage.setItem('view','grid');
		}
			
	}
	
	
	$scope.pinStatus = false;
	$scope.pinUnpin=function(){
		if ($scope.pinStatus == false) {
			$scope.pinStatus = true;
		} else {
			$scope.pinStatus = false;
		}
	}
	
	

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
	  			console.log(respose.data);
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
	var search=[];
    var getNotes=function(){
    	
    	/*var token = localStorage.getItem('token');*/
    	var url = 'getNotes';
    	
    	var notes=noteService.service(url,'GET',notes);
    	
    	notes.then(function(response){
    		$scope.notes=response.data;
    		console.log(response.data);
    		 for (var i = 0; i < response.data.length; i++) {
    			 search.push(response.data[i]);
    		 }
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
    		      },90000);
    		   
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
		if(document.getElementById("title").innerHTML=="" && document.getElementById("body").innerHTML=="" && $scope.imageSrc == "")
			{
				$scope.displayDiv=false;
			}
		else{
			
			$scope.note.pinned=pinStatus;
			$scope.note.color=$scope.color;
			$scope.note.image=$scope.addImg;
			$scope.imageSrc = "";
			$scope.addImg="";
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
    	console.log("deleting Note"+note);
    	var url='delete/'+note.noteId;
    	var notes = noteService.service(url,'DELETE',note);
    	notes.then(function(response) {

			getNotes();
			

		}, function(response) {

			getNotes();
			console.log(response.data);
			$scope.error = response.data;

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
	
	/*//////////////////////////////=====Collaborators NOTE======///////////////////////////// */

	$scope.collaborators = function(note,event)
	{
		console.log("inside collaboarator");
		$mdDialog.show({
			locals:{
				dataToPass : note
			},
			templateUrl : 'template/collaborator.html',
			 parent: angular.element(document.body),
		     targetEvent: event,
		     clickOutsideToClose: true,
		     controllerAs: 'controller',
		     controller: opencollaboratorsModel
		});
	}
	
	function opencollaboratorsModel($scope, $state, dataToPass) {
			
			var getOwner=function(){
				var url = 'getOwner';
				var a= noteService.service(url,'POST',dataToPass)
				a.then(function(response){
					console.log(response.data);
					$scope.owner=response.data;
				},function(response){
					$scope.error=response.data;
				})
			}
		
			var getCollabUser=function(){
				var url = 'getCollabUser';
				var b = noteService.service(url,'POST',dataToPass);
				b.then(function(response){
					$scope.users=response.data;
				},function(response){
					$scope.error=response.data;
				});
			}
			
			$scope.removeCollaborator=function(user){
				/*console.log('remove collab='+user.name);
				var url='removeCollaborator';
				console.log(note);
				var a = noteService.collaborate(url,'POST',dataToPass,user.email);
				a.then(function(response){
					console.log('removed successfully');
					getNotes();
				},function(response){
					console.log("remove collabe fail");
				});*/
				
				var array = dataToPass.collaborator;
				console.log(dataToPass);
				var index = array.indexOf(user);
				array.splice(index, 1);
				update(dataToPass);
				$mdDialog.hide();
			}
			
			
			getOwner();
			getCollabUser();
			
			$scope.getUserEmail = function() {
	      		
	    	/*var url = 'update';*/
	    	console.log($scope.search);
	    	
	    	var url = 'collaborate'; 
	    	var a=noteService.collaborate(url,'POST',dataToPass,$scope.search);
	    	a.then(function(response){
	    		console.log("success");
	    	},function(response){
	    		console.log("Error");
	    	})
	    	
	      }
	
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

	      /*=========================Remove Image=============*/
	      
	      $scope.removeImage=function(mdDialogData){
	    	  mdDialogData.image=null;
	    	  update(mdDialogData);
	      }
	      // Saving the edited note
	      	$scope.saveUpdatedNote = function() {
	    	/*var url = 'update';*/
	    	
	    	console.log(dataToPass);
	    	
	    	dataToPass.title = document.getElementById("updatedNoteTitle").innerHTML;
	    	
	    	dataToPass.body = document.getElementById("updatedNoteBody").innerHTML;
	    	/*var updatedNoteTitle = document.getElementById("updatedNoteTitle").innerHTML;
	    	
	    	var updatedNoteBody = document.getElementById("updatedNoteBody").innerHTML;*/
	    	
	    	update(dataToPass);
			$mdDialog.hide();

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
			
			var User=response.data;
			console.log(User.labels);
			console.log("label"+response.data.labels);
			
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
				console.log(response);
				$scope.error = response.data.responseMessage;

			});
		}
		
		
		/*//////////////////////////////=====UPLOAD Image======///////////////////////////// */
		
		
		
		$scope.openImageUploader = function(type) {
			$scope.type = type;
			console.log(type);
			$('#image').trigger('click');
			return false;
		}
		
		
		$scope.stepsModel = [];

		$scope.imageUpload = function(element){
		    var reader = new FileReader();
		    console.log("ele"+element);
		    reader.onload = $scope.imageIsLoaded;
		    reader.readAsDataURL(element.files[0]);
		    console.log(element.files[0]);
		}
	
		$scope.imageIsLoaded = function(e){
		    $scope.$apply(function() {
		        $scope.stepsModel.push(e.target.result);
		        console.log(e.target.result);
		        var imageSrc=e.target.result;
		        if($scope.type ==='input')
	        	{
		        	
		        	$scope.addImg= imageSrc;
	        	}else{
	        		$scope.type.image=imageSrc;
	        		console.log(e.target.result);
	        		console.log(imageSrc);
	        		update($scope.type);
		        }
		    });
		};
		
		

	/*	$scope.$on("fileProgress", function(e, progress) {
			$scope.progress = progress.loaded / progress.total;
		});
		
		$scope.type = {};
		$scope.type.image = ''; */
		
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
		} else if($state.current.name=='search'){
			$scope.navBarColor = "#512DA8";
			$scope.title = "Google Keep";
		} 
		/*//////////////////////////////=====REFRESH OWNER ======///////////////////////////// */
	
		$scope.refresh=function(){
			$state.reload()

		}
		
		
		/*============================SEARCHING=======================*/
		
		 $scope.querySearch=function(searchText){
			var arr=[];
			j=-1;
		//	console.log('ssdsdas'+search);
			for(var i=0;i<search.length;i++)
				{
					if(searchText==search[i].title){
						j++;
						arr[j]=search[i];
					}
				}
			console.log(arr);
			return arr;
		 }
		 
	      $scope.searchTextChange = function(searchText) {
	          var arr = [];
	          var j = -1;
	          for(var i=0; i<search.length; i++) {
	            if(search[i].title == searchText)  {
	              // console.log(res.data.notes[i]);
	              ++j;
	              arr[j] = search[i];
	            }
	          }
	          $scope.searchResultNotes = arr;
	        }
		    
	   

		/*===========================Create Label========================*/
	      
	      $scope.createLabel=function($event,user){
	    	  $mdDialog.show({
	    		  locals: {
	    		        dataToPass: user  // Pass the note data into dialog box
	    		      },
	    		 templateUrl : 'template/createLabel.html',
	    		 parent : angular.element(document.body),
	    		 targetEvent : event,
	    		 clickOutsideToClose: true,
	    		 controllerAs : 'controller',
	    		 controller : createLabelController
	    	  });
	      }
	      
	      function createLabelController($scope,dataToPass){
	    	  $scope.userlabel=dataToPass;
	    	  $scope.createLabel=function(labelName){
	    		  console.log(labelName)
	    		  $scope.label={};
	    		  $scope.label.name=labelName;
	    		  url = 'addlabel';
	    		  
	    		  var addLabel= noteService.service(url,'POST',$scope.label)
	    		  addLabel.then(function(response){
	    			  console.log("label added successfully");
	    			  $state.reload();
	    			  $mdDialog.hide();
	    		  },function(response){
	    			  console.log("label failed to add")
	    		  })
	    	  }
	      }
	      
	      $scope.labelToggle=function(note,label){
	    	  console.log("clicked");
	    	  
	    	  var index = -1;
	    	  var i=0;
				for ( i = 0; i<note.labels.length;i++) {
					if (note.labels[i].name === label.name) {
						index = i;
						break;
					}
				}

				if (index == -1) {
					note.labels.push(label);
					update(note);
				} else {
					note.labels.splice(index, 1);
					update(note);
				}
	    	  
	      }
	      
			$scope.checkboxCheck = function(note, label) {
				
				var labels = note.labels;
				for (var i = 0; i < labels.length; i++) {
					if (labels[i].name === label.name) {
						return true;
					}
				}
				return false;
			}
			
			/*==========================DELETE LABEL==============================*/
			
			$scope.deleteLabel=function(label){
				var url = 'deleteLabel';
				var deletelabel = noteService.label(url,'POST',label);
				deletelabel.then(function(response){
					console.log("Label deleted successfully");
					$state.reload();
				},function(response){
					console.log("label deletion failed")
				})
			}
			
			/*==========================REMOVE LABEL==============================*/

			$scope.removeLabel=function(note,label){
				console.log("inside remove label");
				var removeLabel = note.labels;
				console.log("inside remove label"+removeLabel);
				var indexOfLabel = removeLabel.indexOf(label);
				removeLabel.splice(indexOfLabel, 1);
				update(note);
				
			}
	      
    getNotes();
    getUser();
    
    
    
  
    
});