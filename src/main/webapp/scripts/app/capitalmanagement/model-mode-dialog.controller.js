'use strict';

angular.module('dEACMApp').controller('ModelModeDialogController', ['$scope', '$state', '$stateParams', '$modalInstance', 'targets', 'dmus', 'capital', 'kaoDistance', 'td', 'executeWithSuperEfficiency', 'executeFunction',
   function($scope, $state, $stateParams, $modalInstance, targets, dmus, capital, kaoDistance, td, executeWithSuperEfficiency, executeFunction) {

		$scope.dmus = dmus;
        $scope.targets = targets;
        $scope.capital = capital;
        $scope.kaoDistance = kaoDistance;
        $scope.td = td;
        $scope.executeWithSuperEfficiency = executeWithSuperEfficiency;
        
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.execute = function(mode) {
        	executeFunction($scope.dmus, $scope.targets, $scope.capital, $scope.kaoDistance, $scope.td, $scope.executeWithSuperEfficiency, mode);
        	$scope.clear();
        };
}]);