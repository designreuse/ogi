function ControllerOwnerAdd($scope, Page, $injector, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log, Utils) {
    $injector.invoke(ControllerOwnerParent, this, {$scope: $scope, $log:$log, $http:$http, ServiceConfiguration:ServiceConfiguration});

    Page.setTitle("Ajouter un propriétaire");

    $scope.setOwner({
        gender: "MO",
        addresses: [Object.create(address)]
    });

    $scope.update = function() {
        $scope.updateTechnical( function() {
            $http.post(ServiceConfiguration.API_URL+"/rest/owner/", $scope.owner)
                .success($scope.callbackSuccess)
                .error($scope.callbackError);
        });
    }
}