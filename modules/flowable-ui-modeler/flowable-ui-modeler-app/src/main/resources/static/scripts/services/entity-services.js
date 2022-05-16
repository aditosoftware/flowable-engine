'use strict';

angular.module('flowableModeler').service('EntityService', ['$http', '$q',
    function ($http, $q) {

        var httpAsPromise = function(options) {
            var deferred = $q.defer();
            $http(options).
                success(function (response, status, headers, config) {
                    deferred.resolve(response);
                })
                .error(function (response, status, headers, config) {
                    deferred.reject(response);
                });
            return deferred.promise;
        };

        this.getConsumers = function () {
            return httpAsPromise({
                method: 'GET',
                url: FLOWABLE.APP_URL.getConsumersUrl(),
                params: {}
            });
        };

    }]);