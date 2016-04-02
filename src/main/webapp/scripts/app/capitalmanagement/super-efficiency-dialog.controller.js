'use strict';

angular.module('dEACMApp').controller('SuperEfficiencyDialogController', ['$scope', '$state', '$stateParams', '$modalInstance', 'targets', 'dmus', 'capital', 'kaoDistance', 'td', 'executeFunction',
   function($scope, $state, $stateParams, $modalInstance, targets, dmus, capital, kaoDistance, td, executeFunction) {

		$scope.dmus = dmus;
        $scope.targets = targets;
        $scope.capital = capital;
        $scope.kaoDistance = kaoDistance;
        $scope.td = td;
        
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.execute = function(executeWithSuperEfficiency) {
        	executeFunction($scope.dmus, $scope.targets, $scope.capital, $scope.kaoDistance, $scope.td, executeWithSuperEfficiency);
        	$scope.clear();
        };
}]);