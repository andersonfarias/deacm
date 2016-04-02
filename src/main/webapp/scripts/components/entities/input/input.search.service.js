'use strict';

angular.module('dEACMApp')
    .factory('InputSearch', function ($resource) {
        return $resource('api/_search/inputs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
