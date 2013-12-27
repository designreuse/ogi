function ControllerOwner($scope, Page, $injector, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log) {
    Page.setTitle("Propriétaire du bien : "+$scope.prp.reference);

    $scope.setOwner({
        gender: "MO",
        addresses: [Object.create(address)]
    });

    // Lecture du propriétaire lié au bien
    $http.get(ServiceConfiguration.API_URL+"/rest/owner/property/"+$routeParams.prpRef)
        .success(function (data, status, headers) {
            if(data.length > 0) {
                $scope.setOwner(data[0]);

                if(!$scope.utils.isUndefinedOrNull($scope.owner.addresses) && $scope.owner.addresses.length > 0) {
                    angular.extend($scope.saveData.addressesOwner[0], $scope.owner.addresses[0]);
                }
            }
        }).error(function (data, status, headers) {
            ServiceAlert.addAlert(status + " : " +data)
        });



    $scope.openCalendar = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };

    $scope.dateOptions = {
        'year-format': "'yyyy'",
        'starting-day': 1
    };
}