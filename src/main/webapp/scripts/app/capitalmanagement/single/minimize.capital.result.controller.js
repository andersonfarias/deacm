'use strict';

angular.module('dEACMApp')
    .controller('SingleDMUMinimizeCapitalResultController', function ($scope, $modal, $state, $stateParams, Principal, CapitalManagement) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        
        $scope.ks = $stateParams.solution.ks;
        $scope.ke = $stateParams.solution.ke;
        $scope.dmu0 = $stateParams.solution.dmu0;
        $scope.minCapital = $stateParams.solution.minCapital;
        $scope.dmus = $stateParams.solution.dmus;
        $scope.kaoDistance = $stateParams.kaoDistance;
        
        $scope.detailDmu = function(index){
        	$modal.open({
                templateUrl: 'scripts/app/capitalmanagement/kao-dialog.html',
                controller: 'KAODialogController',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return $scope.dmus[index];
                    },
                    executedWithDEASuperEfficiency: function(){
                    	return false;
                    }
                }
            });
        };
        
        $scope.goBack = function(){
        	var indexDmu0 = 0;
        	for(var i in $scope.dmus){
        		var dmu = $scope.dmus[i];
        		if( dmu.name == $scope.dmu0.name ) {
        			indexDmu0 = i;
        		}
        		
        		dmu.newRelativeSize = null;
        		dmu.newL2Efficiency = null;
        	}
        	
        	$state.go( 'home.single-minimize-capital' , {
    			"dmus" : $scope.dmus, "kaoDistance": $scope.kaoDistance,
    			"ks": $scope.ks, "dmu0": indexDmu0, "ke": $scope.ke
        	} );
        };
        
        $scope.chooseAnotherModel = function(){
        	$state.go( 'home.models' , { "dmus": $scope.dmus, "kaoDistance": $scope.kaoDistance, "ks": $scope.ks, "ke": $scope.ke, "capital": $scope.minCapital } );
        };
        
    });
