'use strict';

angular.module('dEACMApp')
    .factory('DMUSearch', function ($resource) {
        return $resource('api/_search/dMUs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
