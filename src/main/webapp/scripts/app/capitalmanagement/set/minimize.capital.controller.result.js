'use strict';

angular.module('dEACMApp')
    .controller('SetDMUMinimizeCapitalResultController', function ($scope, $modal, $state, $stateParams, Principal, CapitalManagement) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        
        $scope.mode = $stateParams.solution.mode;
        $scope.td = $stateParams.solution.td;
        $scope.capital = $stateParams.solution.capital;
        $scope.kaoDistance = $stateParams.solution.kaoDistance;
        $scope.targets = $stateParams.solution.targets;
        $scope.dmus = $stateParams.solution.dmus;
        
        $scope.oversized = false;
        $scope.superEfficient = false;
        
        $scope.executedWithDEASuperEfficiency = $stateParams.solution.executeWithSuperEfficiency;
        
        $scope.inputs = [];
        $scope.outputs = [];
        
        if ($scope.dmus) {
        	for(var i in $scope.dmus){
        		var dmu = $scope.dmus[i];
        		dmu.target = false;
        		
        		for(var j in $scope.targets){
        			var target = $scope.targets[j];
        			
        			if( target.oversized ) $scope.oversized = true;
        	        if( target.superEfficient ) $scope.superEfficient = true;
        			
        			if( target.name == dmu.name ) {
        				dmu.target = true;
        				break;
        			}
        		}
        	}
        	
	        for(var i in $scope.dmus[0].inputs){
	        	var input = $scope.dmus[0].inputs[i];
	        	$scope.inputs.push(input.name);
	        }
	        
	        for(var i in $scope.dmus[0].outputs){
	        	var output = $scope.dmus[0].outputs[i];
	        	$scope.outputs.push(output.name);
	        }
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
                    	return $scope.executedWithDEASuperEfficiency; 
                    }
                }
            });
        }
        
        $scope.goBack = function(){
        	for(var i in $scope.dmus){
        		var dmu = $scope.dmus[i];
        		dmu.newRelativeSize = null;
        		dmu.newL2Efficiency = null;
        	}
        	
        	$state.go( 'home.set-minimize-capital' , {
    			"dmus" : $scope.dmus, "kaoDistance": $scope.kaoDistance, "td": $scope.td
        	} );
        };
        
        $scope.chooseAnotherModel = function(){
        	$state.go( 'home.models' , { "dmus": $scope.dmus, "kaoDistance": $scope.kaoDistance, "td": $scope.td, "capital": $scope.capital } );
        };
        
    });
