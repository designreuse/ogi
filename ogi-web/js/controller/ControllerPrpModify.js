function ControllerPrpModify($scope, Page, $injector, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log) {
    $injector.invoke(ControllerPrpParent, this, {$scope: $scope, Page:Page, $log:$log});

    // Get information about current property
    $scope.httpGetCurrentType = $http.get(ServiceConfiguration.API_URL+"/rest/property/"+$routeParams.prpRef).success(function (data) {
        $scope.prp = new PropertyJS(data);
        Page.setTitle("Modifier le bien : "+$scope.prp.reference);
    });

    $scope.prp = new PropertyJS({});

    // Data to save between children controller
    $scope.saveData = {stateOrder: 0};
}



