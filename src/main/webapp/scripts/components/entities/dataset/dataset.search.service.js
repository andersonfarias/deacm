'use strict';

angular.module('dEACMApp')
    .factory('DatasetSearch', function ($resource) {
        return $resource('api/_search/datasets/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
