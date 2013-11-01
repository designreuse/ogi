function ControllerPrpAdd($scope, Page, $injector, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log) {
    $injector.invoke(ControllerPrpParent, this, {$scope: $scope, Page:Page, $log:$log});

    $scope.update = function() {
        $scope.prp.mappingType= "HSE";
        $http.post(ServiceConfiguration.API_URL+"/rest/property/", $scope.prp)
        .success(function (data) {
            $scope.currentType = data;
            Page.setTitle("Ajouter un bien : "+data.label);
        });
    }

    // Get information about current type
    $scope.httpGetCurrentType = $http.get(ServiceConfiguration.API_URL+"/rest/category/"+$routeParams.type).success(function (data) {
        $scope.prp.category = data;
        Page.setTitle("Ajouter un bien : "+data.label);
    });


    $scope.prp = new PropertyJS({});

}



