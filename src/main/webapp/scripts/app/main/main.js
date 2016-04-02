'use strict';

angular.module('dEACMApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/main.html',
                        controller: 'MainController'
                    }
                },
                params:{
                	'dmus': undefined
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        return $translate.refresh();
                    }]
                }
            }).state('home.loaddata', {
                parent: 'site',
                url: '/load',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/load-data.html',
                        controller: 'LoadDataController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('load-data');
                        return $translate.refresh();
                    }]
                }
            }).state('home.dea', {
                parent: 'site',
                url: '/dea',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/dea.html',
                        controller: 'DEAController'
                    }
                },
                params:{
                	'dmus': undefined
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('dea');
                        return $translate.refresh();
                    }]
                }
            }).state('home.kao', {
                parent: 'site',
                url: '/kao',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/kao.html',
                        controller: 'KAOController'
                    }
                },
                params:{
                	'dmus': undefined, 'kaoDistance': undefined
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('kao');
                        return $translate.refresh();
                    }]
                }
            }).state('home.models', {
                parent: 'site',
                url: '/models',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/models.html',
                        controller: 'CapitalManagementModelsController'
                    }
                },
                params:{
                	'dmus': undefined, 'kaoDistance': undefined, "ks": undefined, "ke": undefined, "capital": undefined, "td": undefined
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('cimodels');
                        return $translate.refresh();
                    }]
                }
            }).state('home.single-minimize-capital', {
                parent: 'site',
                url: '/single/minimize/capital',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/single/minimize.capital.html',
                        controller: 'SingleDMUMinimizeCapitalController'
                    }
                },
                params:{
                	'dmus': undefined, "kaoDistance": undefined, "ke": undefined, "dmu0": undefined, "ks": undefined, "targets": undefined
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('single-min-capital');
                        return $translate.refresh();
                    }]
                }
            }).state('home.single-minimize-capital.result', {
                parent: 'site',
                url: '/single/minimize/capital/result',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/single/minimize.capital.result.html',
                        controller: 'SingleDMUMinimizeCapitalResultController'
                    }
                },
                params:{
                	'solution': undefined, "kaoDistance": undefined
                }
            }).state('home.single-maximize-size', {
                parent: 'site',
                url: '/single/maximize/size',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/single/maximize.size.html',
                        controller: 'SingleDMUMaximizeSizeController'
                    }
                },
                params:{
                	'dmus': undefined, "kaoDistance": undefined, "ke": undefined, "dmu0": undefined, "capital": undefined, "targets": undefined
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('single-max-size');
                        return $translate.refresh();
                    }]
                }
            }).state('home.single-maximize-size.result', {
                parent: 'site',
                url: '/single/maximize/size/result',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/single/maximize.size.result.html',
                        controller: 'SingleDMUMaximizeSizeResultController'
                    }
                },
                params:{
                	'solution': undefined, "kaoDistance": undefined 
                }
            }).state('home.single-maximize-efficiency', {
                parent: 'site',
                url: '/single/maximize/efficiency',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/single/maximize.efficiency.html',
                        controller: 'SingleDMUMaximizeEfficiencyController'
                    }
                },
                params:{
                	'dmus': undefined, "kaoDistance": undefined, "ks": undefined, "dmu0": undefined, "capital": undefined, "targets": undefined
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('single-max-efficiency');
                        return $translate.refresh();
                    }]
                }
            }).state('home.single-maximize-efficiency.result', {
                parent: 'site',
                url: '/single/maximize/efficiency/result',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/single/maximize.efficiency.result.html',
                        controller: 'SingleDMUMaximizeEfficiencyResultController'
                    }
                },
                params:{
                	'solution': undefined, "kaoDistance": undefined
                }
            }).state('home.set-minimize-capital', {
                parent: 'site',
                url: '/set/minimize/capital',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/set/minimize.capital.html',
                        controller: 'SetDMUMinimizeCapitalController'
                    }
                },
                params:{
                	'dmus': undefined, 'kaoDistance': undefined, "td": undefined, "targets": undefined, "executeWithSuperefficiency": undefined
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('set-min-capital');
                        return $translate.refresh();
                    }]
                }
            }).state('home.set-minimize-capital.result', {
                parent: 'site',
                url: '/set/minimize/capital/result',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/set/minimize.capital.result.html',
                        controller: 'SetDMUMinimizeCapitalResultController'
                    }
                },
                params:{
                	'solution': undefined
                }
            }).state('home.set-maximize-efficiency', {
                parent: 'site',
                url: '/set/maximize/efficiency',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/set/maximize.efficiency.html',
                        controller: 'SetDMUMaximizeEfficiencyController'
                    }
                },
                params:{
                	'dmus': undefined, 'kaoDistance': undefined, "capital": undefined, "targets": undefined, "executeWithSuperefficiency": undefined
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('set-max-efficiency');
                        return $translate.refresh();
                    }]
                }
            }).state('home.set-maximize-efficiency.result', {
                parent: 'site',
                url: '/set/maximize/efficiency/result',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/capitalmanagement/set/maximize.efficiency.result.html',
                        controller: 'SetDMUMaximizeEfficiencyResultController'
                    }
                },
                params:{
                	'solution': undefined
                }
            });
    });