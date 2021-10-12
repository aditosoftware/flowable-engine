'use strict';

angular.module('flowableModeler').service('DeploymentService', ['$http',
    function ($http)
    {
        //not used
        this.deployProcess = function (pModelId)
        {
            $http({method: 'POST', url: FLOWABLE.APP_URL.getProcessDeployUrl(pModelId)})
                .success(function(data) {})
                .error(function(data, status, headers, config) {});
        };
    }]);

angular.module('flowableModeler').service('AditoUrlService', ['$http',
    function ($http)
    {
        this.getAditoProcessDefinitionUrl = function ()
        {
            var url = null;
            $http({method: 'GET', url: FLOWABLE.APP_URL.getAditoProcessDefinitionUrl()})
                .success(function(data)
                {
                console.log("sucess: " + data);
                    url = data;
                })
                .error(function(data, status, headers, config) {});
                console.log("url: " + url);
            return url;
        };
    }]);