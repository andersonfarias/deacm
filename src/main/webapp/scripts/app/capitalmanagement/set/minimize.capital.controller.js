'use strict';

angular.module('dEACMApp')
    .controller('SetDMUMinimizeCapitalController', function ($scope, $modal, $state, $stateParams, Principal, CapitalManagement) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        
        $scope.td;
        $scope.targets = [];
        $scope.kaoDistance = $stateParams.kaoDistance;
        $scope.dmus = $stateParams.dmus;
        
        $scope.inputs = [];
        $scope.outputs = [];
        
        if ($scope.dmus) {
        	for(var i in $scope.dmus)
        		$scope.targets.push( false );
        	
	        for(var i in $scope.dmus[0].inputs){
	        	var input = $scope.dmus[0].inputs[i];
	        	$scope.inputs.push(input.name);
	        }
	        
	        for(var i in $scope.dmus[0].outputs){
	        	var output = $scope.dmus[0].outputs[i];
	        	$scope.outputs.push(output.name);
	        }
        }
        
        if( $stateParams.td ) $scope.td = $stateParams.td;
        if( $stateParams.kaoDistance ) $scope.kaoDistance = $stateParams.kaoDistance;
        if( $stateParams.dmus ) $scope.dmus = $stateParams.dmus;
        if( $stateParams.targets && $stateParams.targets.length > 0 ){
        	for(var i in $scope.dmus){
        		var dmu = $scope.dmus[i];
        		var result = false;
        		for(var j in $stateParams.targets){
        			var target = $stateParams.targets[j];
        			
        			if(dmu.name == target.name) {
        				result = true;
        				break;
        			}
            	}
        		
        		$scope.targets[i] = result;
        	}
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
        
        $scope.onSelectDMUChangedHandler = function() {}
        
        $scope.validateRequiredNumber = function(number, field) {
        	if( number == null || number == undefined || number == '' ) {
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
        	var targets = [];
        	var dmus = [];
        	
        	for(var i in $scope.targets ){
        		var selected = $scope.targets[i];
        		if( selected ) {
        			targets.push( $scope.dmus[ i ] );
        		}
        	}
        	
        	if( !targets || !targets.length ) {
        		$scope.showErrorMessage( "You must select at least one target DMU!" );
        		return;
        	}
        	
        	if( !$scope.validateRequiredNumber($scope.td, "updating rating") ) return;

        	for(var i in $scope.dmus ){
        		var dmu = $scope.dmus[i];
        		var selected = $scope.targets[i];
        		
        		if( selected ) {
        			if( !$scope.validateRequiredNumber(dmu.coefficientSize, "coefficient of relative size of the target dmu " + dmu.name ) ) return;
        		}
        		
        		for(var j in dmu.inputs) {
        			var input = dmu.inputs[j];
    				
    				if( !$scope.validateRequiredNumber(input.cost, "cost of the input " + input.name + " of the target dmu " + dmu.name ) ) return;
    				if( selected && !$scope.validateRequiredNumber(input.bound, "bound of the input " + input.name + " of the target dmu " + dmu.name ) ) return;
    				
    				input.value = parseFloat(input.value);
        		}
    			
    			for(var j in dmu.outputs) {
    				var output = dmu.outputs[j];
    				
    				if( selected && !$scope.validateRequiredNumber(output.bound, "bound of the output " + output.name + " of the target dmu " + dmu.name ) ) return;
    				
    				output.value = parseFloat(output.value);
    			}
    			
    			dmus.push(dmu);
        	}
        	
        	$modal.open({
                templateUrl: 'scripts/app/capitalmanagement/super-efficiency-dialog.html',
                controller: 'SuperEfficiencyDialogController',
                size: 'lg',
                resolve: {
                	targets: function() { return targets; },
                	dmus: function(){ return dmus; },
                	capital: function(){ return null; },
                	kaoDistance: function() { return $scope.kaoDistance; },
                	td: function(){ return parseFloat($scope.td); },
                	executeFunction: function() {
                			return function(dmus, targets, capital, kaoDistance, td, executeWithSuperefficiency){
                			
                			$modal.open({
                                templateUrl: 'scripts/app/capitalmanagement/model-mode-dialog.html',
                                controller: 'ModelModeDialogController',
                                size: 'lg',
                                resolve: {
                                	targets: function() { return targets; },
                                	dmus: function(){ return dmus; },
                                	capital: function(){ return capital; },
                                	kaoDistance: function() { return kaoDistance; },
                                	td: function(){ return td; },
                                	executeWithSuperEfficiency: function(){ return executeWithSuperefficiency; },
                                	executeFunction: function() {
                                		return function(dmus, targets, capital, kaoDistance, td, executeWithSuperefficiency, mode){
                                			CapitalManagement.setDMUMinCapital(
                                	    			{
                                	    				"targets": targets, "dmus": dmus, "td": td,
                                	    				"kaoDistance": kaoDistance, "executeWithSuperEfficiency": executeWithSuperefficiency,
                                	    				"mode": mode
                                	    			},
                                	    			function(result){
                                	    				$state.go('home.set-minimize-capital.result', { "solution": result });
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
                                		};
                                	}
                                }
                              });
                		}
                	}
                }
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
