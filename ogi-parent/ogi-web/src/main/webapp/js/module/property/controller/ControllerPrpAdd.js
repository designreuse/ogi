function ControllerPrpAdd($scope, Page, $injector, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log) {
    $injector.invoke(ControllerPrpParent, this, {$scope: $scope, Page:Page, $log:$log, $http:$http, ServiceConfiguration:ServiceConfiguration});

    // Get information about current type
    $scope.httpGetCurrentType = $http.get(ServiceConfiguration.API_URL+"/rest/category/"+$routeParams.type).success(function (data) {
        $scope.prp.category = data;
        Page.setTitle("Ajouter un bien : "+data.label);
    });


    $scope.save = function() {
        $scope.updateTechnical(function() {
            $http.post(ServiceConfiguration.API_URL+"/rest/property/", $scope.prp)
                .success(function (data, status) {
                    ServiceAlert.addSuccess("Ajout du bien OK");
                    $scope.prp = new PropertyJS(data);

                    // Reset form to indicate that prp is saved
                    $scope.formPrp.$setPristine(true);
                });
        });
    }

}



