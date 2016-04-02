 'use strict';

angular.module('dEACMApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-dEACMApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-dEACMApp-params')});
                }
                return response;
            }
        };
    });
