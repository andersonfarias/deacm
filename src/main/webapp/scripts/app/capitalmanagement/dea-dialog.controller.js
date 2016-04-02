'use strict';

angular.module('dEACMApp').controller('DEADialogController', ['$scope', '$state', '$stateParams', '$translate', '$translatePartialLoader', '$modalInstance', 'entity', 'Principal',
   function($scope, $state, $stateParams, $translate, $translatePartialLoader, $modalInstance, entity, Principal) {

        $scope.dmu = entity;

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        
}]);