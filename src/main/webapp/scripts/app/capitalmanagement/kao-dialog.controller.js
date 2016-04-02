'use strict';

angular.module('dEACMApp').controller('KAODialogController', ['$scope', '$state', '$stateParams', '$translate', '$translatePartialLoader', '$modalInstance', 'entity', 'Principal', 'executedWithDEASuperEfficiency',
   function($scope, $state, $stateParams, $translate, $translatePartialLoader, $modalInstance, entity, Principal, executedWithDEASuperEfficiency) {

        $scope.dmu = entity;
        
        $scope.showBound = $scope.dmu.inputs[0].bound != null;
        $scope.showCost = $scope.dmu.inputs[0].cost != null;
        $scope.isSingleDMUModel = $scope.dmu.inputs[0].offset != null;
        $scope.showNewWeight = $scope.dmu.inputs[0].newWeight != null;
        $scope.executeWithSuperEfficiency = executedWithDEASuperEfficiency;
        
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        
}]);