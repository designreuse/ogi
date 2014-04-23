function ControllerOwnerModify($scope, Page, $injector, $routeParams, ServiceConfiguration,
                               ServiceAlert, $http, $log, Utils) {
    $injector.invoke(ControllerOwnerParent, this, {$scope: $scope, $log:$log, $http:$http, ServiceConfiguration:ServiceConfiguration});

    // Lecture du propriétaire lié au bien
    $http.get(ServiceConfiguration.API_URL+"/rest/owner/"+$routeParams.techid)
        .success(function (data, status, headers) {
            Page.setTitle("Propriétaire : "+(data.firstname || " ") + (data.surname || ""));
            $scope.setOwner(data);

            if(!$scope.utils.isUndefinedOrNull($scope.owner.addresses) && $scope.owner.addresses.length > 0) {
                angular.extend($scope.saveData.addressesOwner[0], $scope.owner.addresses[0]);
            }
        }).error(function (data, status, headers) {
            ServiceAlert.addAlert(status + " : " +data)
        });

    $scope.save = function() {
        $scope.updateTechnical( function() {
            $http.put(ServiceConfiguration.API_URL+"/rest/owner/"+$scope.owner.techid, $scope.owner)
                .success($scope.callbackSuccess)
                .error($scope.callbackError);
        });
    }

}