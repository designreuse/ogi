function ControllerOwnerModify($scope, Page, $injector, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log, Utils) {
    $scope.owner = null;
    $scope.saveData = {
        addressesOwner: [Object.create(address)]
    };

    $scope.setOwner = function (o) {
        $scope.owner = o;
    }

    $scope.setOwner({
        gender: "MO",
        addresses: [Object.create(address)]
    });


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

    $scope.update = function() {
        // If owner is defined => create / modify it
        if(!Utils.isUndefinedOrNull($scope.owner)) {
            // Il ne faut pas envoyer une adresse vide sinon les contraintes d'intégrités ne sont pas respectées
            $scope.owner.addresses = $scope.saveData.addressesOwner.length == 0 || $scope.saveData.addressesOwner[0].isEmpty() ? [] : $scope.saveData.addressesOwner;

            $http.put(ServiceConfiguration.API_URL+"/rest/owner/"+$scope.owner.techid, $scope.owner)
                .success(function (data, status) {
                    ServiceAlert.addSuccess("Enregistrement du propriétaire OK");
                })
                .error(function (data, status) {
                    ServiceAlert.addError("Erreur lors de l'enregistrement du propriétaire :"+data.message);
                });
        }
    }


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