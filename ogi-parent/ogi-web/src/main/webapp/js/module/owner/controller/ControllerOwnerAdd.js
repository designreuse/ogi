function ControllerOwnerAdd($scope, Page, $injector, $routeParams, ServiceConfiguration,
                            ServiceAlert, $http, $log, Utils, $location) {
    $injector.invoke(ControllerOwnerParent, this, {$scope: $scope, $log:$log, $http:$http, ServiceConfiguration:ServiceConfiguration});

    Page.setTitle("Ajouter un propriétaire");

    $scope.setOwner({
        gender: "MO",
        addresses: [Object.create(address)]
    });

    $scope.save = function() {
        // If user come from "add property" => redirect to this page when owner is created
        $scope.updateTechnical( function() {
            $http.post(ServiceConfiguration.API_URL+"/rest/owner/", $scope.owner)
                .success(function(data, status) {
                    $scope.callbackSuccess(data, status);

                    // Redirect only when url parameter are defined
                    if($location.search().prpReference || $location.search().prpCategory) {
                        var url = "";

                        // Modification
                        if($location.search().prpReference) {
                            url += "/biens/modifier/"+$location.search().prpReference;
                        }
                        // Creation
                        else {
                            url += "/biens/ajouter/"+$location.search().prpCategory;
                        }
                        url +="?ownerTechid="+data.techid;
                        $location.url(url);
                    }
                })
                .error($scope.callbackError);
        });
    }
}