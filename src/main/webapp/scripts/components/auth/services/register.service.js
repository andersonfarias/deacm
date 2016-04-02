'use strict';

angular.module('dEACMApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


