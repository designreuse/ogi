angular.module('myApp.property').controller("ControllerPrpAdd",
function ($scope, Page, $injector, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log, $location) {
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
                    $location.url("/biens/modifier/"+data.reference);

                });
        });
    }

    /**
     * Craft url to create a owner from property module. Only when type/category is read
     * @returns {string}
     */
    $scope.urlOwnerCreate = "";
    $scope.httpGetCurrentType.success(function () {
        var url = "#/proprietaires/ajouter/?";
        url +="prpCategory="+$routeParams.type;
        $scope.urlOwnerCreate = url;
    });

});



