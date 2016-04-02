'use strict';

angular.module('dEACMApp')
    .factory('OutputSearch', function ($resource) {
        return $resource('api/_search/outputs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
