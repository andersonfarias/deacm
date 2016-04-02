'use strict';

angular.module('dEACMApp')
    .controller('KAOController', function ($scope, $modal, $state, $stateParams, Principal, CapitalManagement) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        
        $scope.dmus = $stateParams.dmus;
        $scope.kaoDistance = $stateParams.kaoDistance;
        $scope.inputs = [];
        $scope.outputs = [];
        
        if ($scope.dmus) {
	        for(var i in $scope.dmus[0].inputs){
	        	var input = $scope.dmus[0].inputs[i];
	        	$scope.inputs.push(input.name);
	        }
	        
	        for(var i in $scope.dmus[0].outputs){
	        	var output = $scope.dmus[0].outputs[i];
	        	$scope.outputs.push(output.name);
	        }
        }
        
        $scope.goToChooseModel = function(){
        	$state.go('home.models', {"dmus": $scope.dmus, "kaoDistance": $scope.kaoDistance });
        }
       
        $scope.detailDmu = function(index){
        	$modal.open({
                templateUrl: 'scripts/app/capitalmanagement/kao-dialog.html',
                controller: 'KAODialogController',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return $scope.dmus[index];
                    },
                    executedWithDEASuperEfficiency: function () {
                    	return false; 
                    }
                }
            });
        }
        
        $scope.goBack = function(){
        	$state.go( 'home.dea' , { "dmus" : $scope.dmus } );
        };
    });
