'use strict';

angular.module('flowableModeler').service('JditoProcessesService', ['$http', '$q',
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

        this.getProcesses = function () {
            return httpAsPromise({
                method: 'GET',
                url: FLOWABLE.APP_URL.getJditoProcessesUrl(),
                params: {}
            });
        };

    }]);