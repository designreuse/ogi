function ControllerPrpModify($scope, Page, $injector, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log) {
    $injector.invoke(ControllerPrpParent, this, {$scope: $scope, Page:Page, $log:$log, $http:$http, ServiceConfiguration:ServiceConfiguration});

    // Get information about current property
    $scope.httpGetCurrentType = $http.get(ServiceConfiguration.API_URL+"/rest/property/"+$routeParams.prpRef)
        .success(function (data, status, headers) {
            $scope.prp = new PropertyJS(data);

            $scope.saveData.roof = {type:"ROOF", label:$scope.prp.roof};
            $scope.saveData.wall = {type:"WALL", label:$scope.prp.wall};
            $scope.saveData.insulation = {type:"INSULATION", label:$scope.prp.insulation};
            $scope.saveData.parking = {type:"PARKING", label:$scope.prp.parking};


            Page.setTitle("Modifier le bien : "+$scope.prp.reference);
        }).error(function (data, status, headers) {
           console.error("-->"+data);
        });

    $scope.prp = new PropertyJS({});

    // Data to save between children controller
    $scope.saveData = {stateOrder: 0};
}



