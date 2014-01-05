function ControllerPrpTabDocuments($scope, Page, $routeParams, ServiceConfiguration, ServiceObject, ServiceAlert, $http, $log ,$modal) {


    $scope.httpGetCurrentType.success(function() {
        $scope.linkShopFront = ServiceConfiguration.API_URL+"/rest/report/"+$scope.prp.reference;
    });
}
