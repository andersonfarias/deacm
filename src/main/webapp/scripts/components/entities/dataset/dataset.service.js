'use strict';

angular.module('dEACMApp')
    .factory('Dataset', function ($resource, DateUtils) {
        return $resource('api/datasets/:id', {}, {
        	'save': { method: "POST" },
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
