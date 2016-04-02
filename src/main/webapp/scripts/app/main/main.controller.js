'use strict';

angular.module('dEACMApp')
    .controller('MainController', function ($scope, $modal, $state, $stateParams, $translate, Principal, CapitalManagement) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.init = function() {
        	$( "#main-alert-error-message" ).hide();
        };
        
        $scope.showErrorMessageNotTranslated = function(message) {
			$scope.errorMessage = message;
			$( "#main-alert-error-message" ).fadeTo( 1000 , 1 );
        };
        
        $scope.showErrorMessage = function(message) {
    		$translate( message ).then(function (translation) {
    			$scope.showErrorMessageNotTranslated( translation );
    		});
        };
        
        $scope.closeErrorMessage = function() {
        	$( "#main-alert-error-message" ).hide();
        };
        
        $scope.inputs = [];
        $scope.outputs = [];
        $scope.dmus = [];
        $scope.errorMessage = '';
        
        $scope.initData = function( dmus ) {
        	if( !dmus ) return;
        	
        	for(var i in dmus[0].inputs ){
    			var input = dmus[0].inputs[i];
    			
    			$scope.inputs.push( input.name );
    		}
        	
        	for(var i in dmus[0].outputs ){
    			var output = dmus[0].outputs[i];
    			
    			$scope.outputs.push( output.name );
    		}
        	
        	for(var i in dmus){
        		if( isNaN( i ) ) continue;
        		
        		var dmu = dmus[i];
        		$scope.dmus.push(dmu);
        	}
        };
        
        if( $stateParams.dmus ) $scope.initData( $stateParams.dmus );
        
        $scope.addInput = function(input){
        	if( input ) {
        		if( $scope.inputs.indexOf( input ) >= 0 || $scope.inputs.indexOf( input.toUpperCase() ) >= 0) {
        			$scope.showErrorMessage( 'main.validation.messages.input.unique' );
        			return;
        		}
        		$scope.inputs.push(input);
        		for(var i in $scope.dmus){
        			$scope.dmus[i].inputs.push({name: input, value: 0, cost: 0});
        		}
        	} else {
        		$scope.showErrorMessage( 'main.validation.messages.input.required' );
        	}
        	$scope.inputName = '';
        }
        
        $scope.deleteInput = function(index) {
        	if( index >= 0 ) {
        		$scope.inputs.splice( index , 1 );
        		for(var i in $scope.dmus){
        			$scope.dmus[i].inputs.splice( index , 1 );
        		}
        	}
        }
        
        $scope.addOutput = function(output){
        	if( output ) {
        		if( $scope.outputs.indexOf( output ) >= 0 || $scope.outputs.indexOf( output.toUpperCase() ) >= 0 ) {
        			$scope.showErrorMessage( 'main.validation.messages.output.unique' );
        			return;
        		}
        		$scope.outputs.push(output);
        		for(var i in $scope.dmus){
        			$scope.dmus[i].outputs.push({name: output, value: 0});
        		}
        	} else {
        		$scope.showErrorMessage( 'main.validation.messages.output.required' );
        	}
        	$scope.outputName = '';
        }
        
        $scope.deleteOutput = function(index) {
        	if( index >= 0 ) {
        		$scope.outputs.splice( index , 1 );
        		for(var i in $scope.dmus){
        			$scope.dmus[i].outputs.splice( index , 1 );
        		}
        	}
        }
        
        $scope.addDMU = function() {
        	if( !$scope.inputs.length && !$scope.outputs.length ) {
        		$scope.showErrorMessage( 'main.validation.messages.dmu.input-and-output.required' );
        		return;
        	}
        	if( !$scope.inputs.length ) {
        		$scope.showErrorMessage( 'main.validation.messages.dmu.input.required' );
        		return;
        	}
        	if( !$scope.outputs.length ) {
        		$scope.showErrorMessage( 'main.validation.messages.dmu.output.required' );
        		return;
        	}

        	var dmu = { name: '', inputs: [], outputs: [] };
        	
        	for(var i in $scope.inputs){
        		var input = $scope.inputs[ i ];
        		dmu.inputs.push( {name: input, value: null} );
        	}
        	
        	for(var i in $scope.outputs){
        		var output = $scope.outputs[ i ];
        		dmu.outputs.push( {name: output, value: null} );
        	}
        	
        	$scope.dmus.push(dmu);
        }
        
        $scope.deleteDmu = function(index){
        	$scope.dmus.splice( index , 1 );
        }
        
        $scope.calculateDEA = function(){
        	var dmus = $scope.dmus;
    		for(var i in dmus){
    			var dmu = dmus[i];
    			
    			if( !dmu.name ) {
    				$scope.showErrorMessageNotTranslated( "It's required to give all DMUs name!" );
    				return;
    			}
    			
    			for(var j in dmu.inputs){
    				var input = dmu.inputs[j];
    				
    				if( !input.value ) {
    					$scope.showErrorMessageNotTranslated( "It's required to give all inputs values!" );
    					return;
    				}
    				
    				if( isNaN( input.value ) ) {
    					$scope.showErrorMessageNotTranslated( "The inputs values must be numbers" );
    					return;
    				}
    				
    				input.value = parseFloat(input.value);
    			}
    			
    			for(var j in dmus[i].outputs){
    				var output = dmu.outputs[j];
    				
    				if( !output.value ) {
    					$scope.showErrorMessageNotTranslated( "It's required to give all outputs values!" );
    					return;
    				}
    				
    				if( isNaN( output.value ) ) {
    					$scope.showErrorMessageNotTranslated( "The outputs values must be numbers" );
    					return;
    				}
    				
    				output.value = parseFloat(output.value);
    			}
    		}
    		
    		CapitalManagement.dea(dmus, function(success){
    			$state.go('home.dea', {"dmus": success});
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
        
        $scope.loadCSVFile = function(){
        	$modal.open({
                templateUrl: 'scripts/app/main/load-csv-dialog.html',
                controller: 'LoadCSVDialogController',
                size: 'md'
            });
        };
        
        $scope.init();
    });
