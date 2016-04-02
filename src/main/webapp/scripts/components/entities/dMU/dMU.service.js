'use strict';

angular.module('dEACMApp')
    .factory('DMU', function ($resource, DateUtils) {
        return $resource('api/dMUs/:id', {}, {
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
