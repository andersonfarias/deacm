'use strict';

angular.module('dEACMApp')
    .controller('DEAController', function ($scope, $modal, $state, $stateParams, Principal, CapitalManagement) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        
        $scope.dmus = $stateParams.dmus;
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
        
        $scope.calculateKAO = function(){
        	var dmus = $scope.dmus;
    		for(var i in dmus){
    			
    			for(var j in dmus[i].inputs){
    				dmus[i].inputs[j].value = parseFloat(dmus[i].inputs[j].value);
    			}
    			
    			for(var j in dmus[i].outputs){
    				dmus[i].outputs[j].value = parseFloat(dmus[i].outputs[j].value);
    			}
    		}
    		
    		CapitalManagement.kao(dmus, function(success){
    			$state.go('home.kao', { "dmus": success.dmus, "kaoDistance": success.kaoDistance});
    		}, function(error){
    			var message = error.statusText;
    			if( error.data.message ) message = error.data.message;
    			
    			$modal.open({
                    templateUrl: 'scripts/app/main/dea-error-dialog.html',
                    controller: 'DEAErrorDialogController',
                    size: 'md',
                    resolve: { "message": function(){ return message } }
                });
    		});
        }
        
        $scope.detailDmu = function(index){
        	$modal.open({
                templateUrl: 'scripts/app/capitalmanagement/dea-dialog.html',
                controller: 'DEADialogController',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return $scope.dmus[index];
                    }
                }
            });
        }
        
        $scope.goBack = function() {
        	$state.go('home', { "dmus": $scope.dmus });
        };
    });
