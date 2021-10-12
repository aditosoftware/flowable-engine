angular.module('flowableModeler').controller('FlowableJditoProcessCtrl',
    ['$scope', '$modal', '$timeout', '$translate', function ($scope, $modal, $timeout, $translate) {

        // Config for the modal window
        var opts = {
            template: 'editor-app/configuration/properties/jditoprocess-popup.html?version=' + Date.now(),
            scope: $scope
        };

        // Open the dialog
        _internalCreateModal(opts, $modal, $scope);
    }]);

angular.module('flowableModeler').controller('FlowableJditoProcessDisplayCtrl', [ '$scope', function ($scope) {

    if ($scope.property.value !== undefined && $scope.property.value !== null
        && $scope.property.value.fields !== undefined
        && $scope.property.value.fields !== null
        && $scope.property.value.fields.length !== 0) {

        var process = $scope.property.value.fields.find(field => field.name == 'jditoProcess');
        $scope.inputProcess = process ? process.implementation : null;
    } else {
        $scope.inputProcess = null;
    }
}]);

angular.module('flowableModeler').controller('FlowableJditoProcessPopupCtrl',
    ['$scope', '$q', '$translate', '$timeout', 'JditoProcessesService', 'TaskVariablesService', function ($scope, $q, $translate, $timeout, JditoProcessesService, TaskVariablesService) {

        var PROCESS_PROPERTYNAME = 'jditoProcess';
        var VARIABLES_PROPERTYNAME = 'taskVariables';

        $scope.getVariableDisplayValue = function (pVariable)
        {
            switch (pVariable.type)
            {
                case 'enum':
                    var enumRow = pVariable.items.find(row => row.id == pVariable.value);
                    return enumRow != undefined ? enumRow.name : pVariable.value;
                case 'boolean':
                    return pVariable.value ? 'Yes' : 'No';
                case 'date':
                    if (pVariable.value instanceof Date)
                        return pVariable.value.toLocaleDateString();
                    else if (pVariable.value)
                        return new Date(pVariable.value).toLocaleDateString();
                    else
                        return '';
                case 'string':
                case 'number':
                default:
                    return pVariable.value;
            }
        }

        $scope.loadVariables = function () {

            TaskVariablesService.getVariables($scope.inputProcess, JSON.stringify($scope.variableValues)).then(function (result) {
                if ($scope.fields.length > 0)
                    $scope.fields.splice(0, $scope.fields.length); //splice to not change the reference

                result.forEach(obj => {
                    if (!obj.name)
                        obj.name = obj.id;
                    if (obj.type == 'enum' && obj.items == undefined)
                        obj.items = [];
                    if ($scope.variableValues[obj.id])
                        obj.value = $scope.variableValues[obj.id].value;
                    else
                        obj.value = obj.defaultValue || '';

                    obj.displayValue = $scope.getVariableDisplayValue(obj);

                    $scope.fields.push(obj);
                });
            });
        };

        $scope.writeVariables = function () {
            $scope.variableValues = {};
            $scope.fields.forEach(field => {$scope.variableValues[field.id] = {value: field.value};});
        };

        $scope.processChanged = function () {
            $scope.variableValues = {};
            var selectedItems = $scope.gridApi.selection.getSelectedRows();
            if (selectedItems && selectedItems.length > 0)
                $scope.gridApi.selection.toggleRowSelection(selectedItems[0]);
            $scope.loadVariables();
        };

        $scope.variableValueChanged = function () {
            if ($scope.selectedField.type == 'number' && !Number.isNaN($scope.selectedField.numberValue))
                $scope.selectedField.value = $scope.selectedField.numberValue;
            else if ($scope.selectedField.type == 'date')
                $scope.selectedField.value = $scope.selectedField.dateValue;
            $scope.selectedField.displayValue = $scope.getVariableDisplayValue($scope.selectedField);

            //relational -> other variables depend on the value of the variable
            if ($scope.selectedField.relational)
            {
                $scope.writeVariables();
                $scope.loadVariables();
            }
        };

        var processes = [];
        JditoProcessesService.getProcesses().then(function (result) {
            for (let id in result)
                processes.push({id: id, name: result[id]});
        });
        $scope.processes = processes;

        // Put json representing form properties on scope
        if ($scope.property.value !== undefined && $scope.property.value !== null
            && $scope.property.value.fields !== undefined
            && $scope.property.value.fields !== null) {

            $scope.variableValues = {};
            $scope.fields = [];

            $scope.property.value.fields.forEach(field => {
                if (field.name === PROCESS_PROPERTYNAME)
                    $scope.inputProcess = field.stringValue;
                else if (field.name === VARIABLES_PROPERTYNAME)
                    $scope.variableValues = JSON.parse(field.stringValue);
            });

            $scope.loadVariables();

        } else {
            $scope.fields = [];
            $scope.inputProcess = null;
        }

        $scope.translationsRetrieved = false;
        $scope.labels = {};

        var namePromise = $translate('PROPERTY.FIELDS.PARAMETER');
        var valuePromise = $translate('PROPERTY.FIELDS.VALUE');

        $q.all([namePromise, valuePromise]).then(function (results) {
            $scope.labels.nameLabel = results[0];
            $scope.labels.valueLabel = results[1];
            $scope.translationsRetrieved = true;

            // Config for grid
            $scope.gridOptions = {
                data: $scope.fields,
                headerRowHeight: 28,
                enableRowSelection: true,
                enableRowHeaderSelection: false,
                multiSelect: false,
                modifierKeysToMultiSelect: false,
                enableHorizontalScrollbar: 0,
                enableColumnMenus: false,
                enableSorting: false,
                columnDefs: [{field: 'name', displayName: $scope.labels.nameLabel},
                    {field: 'displayValue', displayName: $scope.labels.valueLabel}]
            };

            $scope.gridOptions.onRegisterApi = function (gridApi) {
                //set gridApi on scope
                $scope.gridApi = gridApi;
                gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                    $scope.selectedField = row.entity;
                    if ($scope.selectedField.type == 'number')
                        $scope.selectedField.numberValue = Number($scope.selectedField.value);
                    else if ($scope.selectedField.type == 'date')
                        $scope.selectedField.dateValue = new Date($scope.selectedField.value);
                });
            };
        });

        // Click handler for save button
        $scope.save = function () {
            $scope.property.value = {fields : []};

            if ($scope.inputProcess) {
                $scope.property.value.fields.push({
                    name : PROCESS_PROPERTYNAME,
                    implementation : $scope.inputProcess,
                    stringValue : $scope.inputProcess,
                    expression : '',
                    string : ''

                });
            }
            if ($scope.fields.length > 0) {
                $scope.writeVariables();
                valueObj = JSON.stringify($scope.variableValues);
                $scope.property.value.fields.push({
                    name : VARIABLES_PROPERTYNAME,
                    implementation : valueObj,
                    stringValue : valueObj,
                    expression : '',
                    string : ''
                });
            }
            if ($scope.property.value.fields.length === 0)
                $scope.property.value = null;

            $scope.updatePropertyInModel($scope.property);
            $scope.close();
        };

        $scope.cancel = function () {
            $scope.$hide();
        };

        // Close button handler
        $scope.close = function () {
            $scope.property.mode = 'read';
            $scope.$hide();
        };
    }]);
