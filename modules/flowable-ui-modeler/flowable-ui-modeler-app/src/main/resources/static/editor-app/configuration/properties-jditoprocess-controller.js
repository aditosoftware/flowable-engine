angular.module('flowableModeler').controller('FlowableJditoProcessDisplayCtrl', [ '$scope', function ($scope) {

    if ($scope.property.value !== undefined && $scope.property.value !== null
        && $scope.property.value.fields !== undefined
        && $scope.property.value.fields !== null
        && $scope.property.value.fields.length !== 0) {

        $scope.inputProcess = $scope.property.value.fields[0].implementation;
    } else {
        $scope.inputProcess = null;
    }
}]);

angular.module('flowableModeler').controller('FlowableJditoProcessPropertyCtrl', [ '$scope', 'JditoProcessesService', function ($scope, JditoProcessesService) {

    var processes = [];
    JditoProcessesService.getProcesses().then(function (result) {
        for (let id in result)
            processes.push({id: id, name: result[id]});
    });
    $scope.processes = processes;

	if ($scope.property.value !== undefined && $scope.property.value !== null
        && $scope.property.value.fields !== undefined
        && $scope.property.value.fields !== null
        && $scope.property.value.fields.length !== 0) {

        $scope.inputProcess = $scope.property.value.fields[0].implementation;
    } else {
        $scope.inputProcess = null;
    }

	$scope.setProcess = function () {
	    if ($scope.inputProcess) {
            $scope.property.value = {
                fields : [{
                    name: 'jditoProcess',
                    implementation: $scope.inputProcess,
                    stringValue: $scope.inputProcess,
                    expression: '',
                    string: ''
                 }]
            };
	    } else {
	        $scope.property.value = null;
	    }
	};

    $scope.processChanged = function() {
    	$scope.setProcess();
        $scope.updatePropertyInModel($scope.property);
    };
}]);