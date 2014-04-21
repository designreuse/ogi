function ControllerPrpTabOwner($scope, Page, $injector, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log, ServiceObject, Utils) {
    Page.setTitle("Propriétaire du bien : "+$routeParams.prpRef);

    $scope.owners = [];         // All owners
    $scope.ownersProperty = []; // Owners links to current property


    // Read all owners to display in a table
    $http.get(ServiceConfiguration.API_URL+"/rest/owner/").success(function (data) {
        $scope.owners = data;
    });

    // Read owners link to current property. Only if property reference exist
    if(!Utils.isUndefinedOrNull($scope.prp.reference)) {
        $http.get(ServiceConfiguration.API_URL+"/rest/owner/property/"+$scope.prp.reference)
            .success(function (data, status, headers) {
                $scope.ownersProperty = data;
        });
    }

    /**
     * Associate an owner with a property
     * @param techid owner techical id
     */
    $scope.addToProperty = function (techid) {
        var ownerToAdd = ServiceObject.getObject($scope.owners, {"techid" : techid}, ["techid"]);

        $http.put(ServiceConfiguration.API_URL+"/rest/owner/"+ownerToAdd.techid+"/property/"+$routeParams.prpRef, null)
            .success(function (data, status) {
                $scope.ownersProperty.push(ownerToAdd);
                ServiceAlert.addSuccess("Liaison entre le propriétaire et le bien effectuée");
            })
            .error(function (data, status) {
                ServiceAlert.addError("Errreur lors de la liaison entre le propriétaire et le bien : "+data.message);
            });
    }

    $scope.deleteToProperty = function (techid) {
        var ownerToDelete = ServiceObject.getObject($scope.owners, {"techid" : techid}, ["techid"]);

        $http.delete(ServiceConfiguration.API_URL+"/rest/owner/"+ownerToDelete.techid+"/property/"+$routeParams.prpRef, null)
            .success(function (data, status) {
                var index = $scope.ownersProperty.indexOf(ownerToDelete);
                if (index > -1) {
                    $scope.ownersProperty.splice(index, 1);
                }
                ServiceAlert.addSuccess("Suppression de la liaison entre le propriétaire et le bien effectuée");
            })
            .error(function (data, status) {
                ServiceAlert.addError("Errreur lors de la suppression de la liaison entre le propriétaire et le bien : "+data.message);
            });
    }


    $scope.displayZoneOwnerLink = function() {
        return $scope.ownersProperty.length > 0;
    }

}