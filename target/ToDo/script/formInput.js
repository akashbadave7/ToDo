angular.module('ToDo')
.controller('inputController', inputController);

 function inputController ($scope) {
    $scope.project = {
       comments: 'Comments',    
    };
 } 