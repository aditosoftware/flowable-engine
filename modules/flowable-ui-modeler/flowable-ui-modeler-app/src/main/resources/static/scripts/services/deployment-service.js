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

angular.module('flowableModeler').service('AditoUrlService', ['$http', '$window',
    function ($http, $window)
    {
        this.openProcessDefinitionUrl = function ()
        {
            $http({method: 'GET', url: FLOWABLE.APP_URL.getAditoProcessDefinitionUrl()})
                .success(function(aditoUrl)
                {
                    if (aditoUrl)
                        $window.open(aditoUrl, "_self");
                })
                .error(function(data, status, headers, config) {});

            return url;
        };
    }]);