'use strict';

angular.module('dEACMApp')
    .factory('CapitalManagement', function ($resource, DateUtils) {
        return $resource('api/dea/', {}, {
            'dea': { method: 'POST', isArray: true },
            'kao': { url: 'api/kao', method: 'POST' },
            'singleDMUMinCapital': { url: 'api/single/min/capital', method: 'POST' },
            'singleDMUMaxSize': { url: 'api/single/max/size', method: 'POST' },
            'singleDMUMaxEfficiency': { url: 'api/single/max/efficiency', method: 'POST' },
            'setDMUMinCapital': { url: 'api/set/min/capital', method: 'POST' },
            'setDMUMaxEfficiency': { url: 'api/set/max/efficiency', method: 'POST' }
        });
    });
