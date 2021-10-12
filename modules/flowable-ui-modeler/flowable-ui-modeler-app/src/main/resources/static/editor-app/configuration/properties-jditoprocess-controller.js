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

angular.module('flowableModeler').controller('FlowableJditoProcessPropertyCtrl', [ '$scope', function ($scope) {

	$scope.shapeId = $scope.selectedShape.id;
	$scope.valueFlushed = false;

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

    /** Handler called when input field is blurred */
    $scope.inputBlurred = function() {
    	$scope.valueFlushed = true;
    	if ($scope.inputProcess) {
    		$scope.inputProcess = $scope.inputProcess.replace(/(<([^>]+)>)/ig,"");
    	}
    	$scope.setProcess();
        $scope.updatePropertyInModel($scope.property);
    };

    $scope.enterPressed = function(keyEvent) {
        // if enter is pressed
        if (keyEvent && keyEvent.which === 13) {
            keyEvent.preventDefault();
            $scope.inputBlurred(); // we want to do the same as if the user would blur the input field
        }
        // else; do nothing
    };

    $scope.$on('$destroy', function controllerDestroyed() {
    	if(!$scope.valueFlushed) {
    		if ($scope.inputProcess) {
        		$scope.inputProcess = $scope.inputProcess.replace(/(<([^>]+)>)/ig,"");
        	}
        	$scope.setProcess();
    		$scope.updatePropertyInModel($scope.property, $scope.shapeId);
    	}
    });

}]);