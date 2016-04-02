'use strict';

angular.module('dEACMApp').controller('LoadCSVDialogController', function($scope, $modalInstance, Upload, $state) {

	$scope.errorMessage = '';
	
	$scope.init = function() {
    	$( "#load-csv-dialog-error-message" ).hide();
    };
	
    $scope.clear = function() {
        $modalInstance.dismiss('cancel');
    };
    
    $scope.showErrorMessage = function(message) {
		$scope.errorMessage = message;
		$( "#load-csv-dialog-error-message" ).fadeTo( 1000 , 1 );
    };
    
    $scope.closeErrorMessage = function() {
    	$( "#load-csv-dialog-error-message" ).hide();
    };
    
    $scope.uploadFile = function(file){
    	
    	if( !file ) {
    		$scope.showErrorMessage( 'It\'s required to select the csv file.' );
    		return;
    	}
    	
    	// DEPLOY
    	// alterar de localhost para o IP da máquina
    	
	    file.upload = Upload.upload({
	    	method: 'POST',
//	        url: 'http://52.91.19.81:8080/deacm/api/upload',
	    	url: 'http://localhost:8080/api/upload',
//	    	url: 'http://localhost:8080/deacm/api/upload', //outros
	        data: { file: file, inputs: $scope.inputs, outputs: $scope.outputs }
	    });
	
	    file.upload.then(function (response) {
	    	$scope.clear();
	    	
	    	var models = {
    			"1": 'home.single-maximize-efficiency',
    			"2": 'home.single-maximize-size',
    			"3": 'home.single-minimize-capital',
    			"4": 'home.set-minimize-capital',
    			"5": 'home.set-maximize-efficiency'
	    	};
	    	
	    	$state.go(models[response.data.modelCode], {
	    		"dmus": response.data.solution.dmus, "kaoDistance": response.data.solution.kaoDistance, "targets": response.data.targets,
	    		"td": response.data.td, "capital": response.data.capital, "ks": response.data.ks, "ke": response.data.ke
	    	});
	    	
	    }, function (response) {
	        $scope.showErrorMessage( response.data.message );
	    });
    };
   
    $scope.init();
});