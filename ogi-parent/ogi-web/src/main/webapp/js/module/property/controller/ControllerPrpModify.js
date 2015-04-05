angular.module('myApp.property').controller("ControllerPrpModify",
function ($scope, $rootScope, Page, $injector, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log, $filter) {
    $injector.invoke(ControllerPrpParent, this, {$scope: $scope, Page:Page, $log:$log, $http:$http, ServiceConfiguration:ServiceConfiguration});
    $scope.formCreate = false;

    Page.setTitle("Modifier le bien : "+$routeParams.prpRef);

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
            $scope.saveData.heating = {type:"HEATING", label:$scope.prp.heating};
            $scope.saveData.parking = {type:"PARKING", label:$scope.prp.parking};
            $scope.saveData.sanitation = {type:"SANITATION", label:$scope.prp.sanitation};
            $scope.saveData.type = {label : $scope.prp.type};
            $scope.saveData.buildDate = $filter('date')($scope.prp.buildDate , "yyyy");

            $scope.saveData.address = Object.create(address);
            if(!$scope.utils.isUndefinedOrNull($scope.prp.address)) {
                angular.extend($scope.saveData.address, $scope.prp.address);
            }
        });

    $scope.httpGetCurrentType.success(function() {
        // Read owners link to current property.
        $http.get(ServiceConfiguration.API_URL+"/rest/owner/property/"+$scope.prp.reference)
            .success(function (data, status, headers) {
                $scope.saveData.ownersProperty = $scope.saveData.ownersProperty.concat(data);
            });
    });

    $scope.save = function() {
        $scope.updateTechnical(function() {
            $http.put(ServiceConfiguration.API_URL+"/rest/property/"+$scope.prp.reference, $scope.prp)
                .success(function (data, status) {
                    ServiceAlert.addSuccess("Modification du bien OK");
                    $scope.prp = new PropertyJS(data);

                    // Send event (to ControllerPrpTabAdministratif)
                    $rootScope.$broadcast('event-prp-update', $scope.prp);
                });
        });
    }

    /**
     * Craft url to create a owner from property module. Only when prp is read
     * @returns {string}
     */
    $scope.urlOwnerCreate = "";
    $scope.httpGetCurrentType.success(function () {
        var url = "#/proprietaires/ajouter/?";
        url +="prpCategorie="+$scope.prp.category.code;
        url += "&prpReference="+$scope.prp.reference;
        $scope.urlOwnerCreate = url;
    });
});



