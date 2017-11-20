var ToDo = angular.module('ToDo')

ToDo.controller('homeController', function ($scope, $timeout, $mdSidenav) {
    $scope.toggleLeft = buildToggler('left');
   /* $scope.toggleRight = buildToggler('right');
*/
    function buildToggler(componentId) {
      return function() {
        $mdSidenav(componentId).toggle();
      };
    }
  
    
    $scope.displayDiv=false;
    $scope.showDiv=function()
    {
    	$scope.displayDiv=true;
    }
});