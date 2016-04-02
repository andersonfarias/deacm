'use strict';

angular.module('dEACMApp')
	.controller('DEAErrorDialogController', function($scope, $modalInstance, message) {

        $scope.error = message;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirm = function () {
        	$modalInstance.close(true);
        };

    });