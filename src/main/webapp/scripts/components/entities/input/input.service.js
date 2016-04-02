'use strict';

angular.module('dEACMApp')
    .factory('Input', function ($resource, DateUtils) {
        return $resource('api/inputs/:id', {}, {
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
