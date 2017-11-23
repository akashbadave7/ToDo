var ToDo = angular.module('ToDo');

ToDo.factory('noteService', function($http,$location) {
	var notes={};

	
	notes.service=function(url,method,note){
		return $http({	
		    method: method,
		    url: url,
		    data:note,
		    headers: {
		        'Authorization': localStorage.getItem('token')
		    }
		
		});
	}
/*	notes.getNotes=function(token){
	return $http({
	    method: 'GET',
	    url: 'getNotes',
	    headers: {
	        'Authorization': token
	    }
	
	});
	
	}
	notes.addNote=function(token,note){
		console.log("Inside add note");
		console.log(note);

		return $http({

		    method: 'POST',

		    url: 'addNote',

		    data:note,

		    headers: {

		        'Authorization': token

		    }
		});
	}*/
	return notes;
})