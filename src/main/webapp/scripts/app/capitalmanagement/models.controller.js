'use strict';

angular.module('dEACMApp')
    .controller('CapitalManagementModelsController', function ($scope, $modal, $state, $stateParams, Principal, CapitalManagement) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        
        $scope.td = $stateParams.td;
        $scope.ks = $stateParams.ks;
        $scope.ke = $stateParams.ke;
        $scope.capital = $stateParams.capital;
        
        $scope.dmus = $stateParams.dmus;
        $scope.kaoDistance = $stateParams.kaoDistance;
        
        $scope.chooseModel = function(model){
        	$state.go( model , { "dmus": $scope.dmus, "kaoDistance": $scope.kaoDistance, "ks": $scope.ks, "ke": $scope.ke, "capital": $scope.capital, "td": $scope.td } );
        };
       
        $scope.goBack = function(){
        	$state.go( 'home.kao' , { "dmus": $scope.dmus, "kaoDistance": $scope.kaoDistance } );
        };
    });
