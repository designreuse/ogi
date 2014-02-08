function ControllerPrpTabPartner($scope, Page, $routeParams, ServiceConfiguration, ServiceObject, ServiceAlert, $http, $log, Utils) {

    $scope.partners = [];
    $scope.partners.push({"name":"acimflo", "img":"img/LogoAcimflo.png", "exist": null});
    $scope.partners.push({"name":"diaporama", "img":"img/LogoDiaporama.png", "exist": null});

    $scope.httpGetCurrentType.success(function() {
        $scope.partners.forEach(function(elt, index, array) {
            $http.get(ServiceConfiguration.API_URL+"/rest/synchronisation/"+elt.name+"/"+$scope.prp.reference)
                .success(function (data, status, headers) {
                    elt.exist=data.exist;
                });
        });
    });


    $scope.synchronize = function(partner) {
        var references = [$scope.prp.reference];
        $http.post(ServiceConfiguration.API_URL+"/rest/synchronisation/"+partner+"/", references)
            .success(function (data, status) {


            });
    }

};

