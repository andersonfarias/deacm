'use strict';

angular.module('dEACMApp')
    .controller('SingleDMUMaximizeEfficiencyController', function ($scope, $modal, $state, $stateParams, Principal, CapitalManagement) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        
        $scope.ks;
        $scope.capital;
        $scope.dmu0 = { index: 0 };
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
        
        if( $stateParams.ks ) $scope.ks = $stateParams.ks;
        if( $stateParams.capital ) $scope.capital = $stateParams.capital;
        if( $stateParams.dmu0 ) {
        	$scope.dmu0 = { index: $stateParams.dmu0 };
        	for(var i in $scope.dmus ){
        		var dmu = $scope.dmus[i];
        		if( i != $scope.dmu0.index ) {
        			
        			for(var j in dmu.inputs){
        				var input = dmu.inputs[j];
        				input.cost = null;
        				input.bound = null;
        			}
        			
        			for(var j in dmu.outputs){
        				var output = dmu.outputs[j];
        				output.bound = null;
        			}
        		}
        	}
        }
        if( $stateParams.targets && $stateParams.targets.length && $stateParams.targets.length == 1 ){
        	var index = 0;
        	for(var i in $scope.dmus){
        		if($scope.dmus[i].name == $stateParams.targets[0].name) {
        			index = i;
        			break;
        		}
        	}
        	
        	$scope.dmu0 = { index: index };
        }
        
        $scope.init = function() {
        	$( "#alert-error-message" ).hide();
        };
        
        $scope.showErrorMessage = function(message) {
			$scope.errorMessage = message;
			$( "#alert-error-message" ).fadeTo( 1000 , 1 );
        };
        
        $scope.closeErrorMessage = function() {
        	$( "#alert-error-message" ).hide();
        };
        
        $scope.onSelectDMUChangedHandler = function() { }
        
        $scope.validateRequiredNumber = function(number, field) {
        	if( number == null || number == undefined ) {
        		$scope.showErrorMessage( "It's required to inform the " + field + "!" );
        		return false;
        	}
        	
        	if( isNaN( number ) ) {
        		$scope.showErrorMessage( "The " + field + " must be a number!" );
        		return false;
        	}
        	
        	return true;
        }
        
        $scope.execute = function(){
        	var dmu0 = null;
        	if( $scope.dmu0 ) {
        		for(var i in $scope.dmus){
        			var dmu = $scope.dmus[i];
        			if( i == $scope.dmu0.index ){
        				dmu0 = dmu;
        				break;
        			}
        		}
        	}

        	if( !dmu0 ) {
        		$scope.showErrorMessage( "You must select a target DMU!" );
        		return;
        	}
        	
        	if( !$scope.validateRequiredNumber($scope.ks, "relative size coefficient") ) return;
        	if( !$scope.validateRequiredNumber($scope.capital, "capital") ) return;

			for(var j in dmu0.inputs) {
				var input = dmu0.inputs[j];
				
				if( !$scope.validateRequiredNumber(input.cost, "cost of the input " + input.name + " of the target dmu" ) ) return;
				if( !$scope.validateRequiredNumber(input.bound, "bound of the input " + input.name + " of the target dmu" ) ) return;
				
				input.value = parseFloat(input.value);
			}
			
			for(var j in dmu0.outputs) {
				var output = dmu0.outputs[j];
				
				if( !$scope.validateRequiredNumber(output.bound, "bound of the output " + output.name + " of the target dmu" ) ) return;
				
				output.value = parseFloat(output.value);
			}
    		
    		CapitalManagement.singleDMUMaxEfficiency(
    			{"target": dmu0, "ks": parseFloat($scope.ks), "capital": parseFloat($scope.capital), "dmus": $scope.dmus},
    			function(result){
    				$state.go('home.single-maximize-efficiency.result', { "solution": result, "kaoDistance": $scope.kaoDistance });
    			},
    			function(error){
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
        };
        
        $scope.goBack = function() {
        	$state.go( 'home.models' , { "dmus" : $scope.dmus, "kaoDistance": $scope.kaoDistance } );
        };
        
        $scope.init();
    });
