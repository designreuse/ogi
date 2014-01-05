function ControllerPrpModify($scope, Page, $injector, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log, $filter) {
    $injector.invoke(ControllerPrpParent, this, {$scope: $scope, Page:Page, $log:$log, $http:$http, ServiceConfiguration:ServiceConfiguration});

    // Get information about current property
    $scope.httpGetCurrentType = $http.get(ServiceConfiguration.API_URL+"/rest/property/"+$routeParams.prpRef)
        .success(function (data, status, headers) {
            $scope.prp = new PropertyJS(data);

            if(!$scope.utils.isUndefinedOrNull($scope.prp.state)) {
                $scope.saveData.stateOrder = $scope.prp.state.order;
            }
            $scope.saveData.roof = {type:"ROOF", label:$scope.prp.roof};
            $scope.saveData.wall = {type:"WALL", label:$scope.prp.wall};
            $scope.saveData.insulation = {type:"INSULATION", label:$scope.prp.insulation};
            $scope.saveData.parking = {type:"PARKING", label:$scope.prp.parking};
            $scope.saveData.type = {label : $scope.prp.type};

            $scope.saveData.address = Object.create(address);
            if(!$scope.utils.isUndefinedOrNull($scope.prp.address)) {
                angular.extend($scope.saveData.address, $scope.prp.address);
            }

            $scope.saveData.buildDate = $filter('date')($scope.prp.buildDate , "yyyy");


            Page.setTitle("Modifier le bien : "+$scope.prp.reference);
        }).error(function (data, status, headers) {
           console.error("-->"+data);
        });
}



