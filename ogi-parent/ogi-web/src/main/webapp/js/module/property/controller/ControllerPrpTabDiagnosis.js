function ControllerPrpTabDiagnosis($scope, Page, $routeParams, ServiceConfiguration, ServiceDPE) {

    $scope.API_URL = ServiceConfiguration.API_URL;

    // Watch change for dpe values to update classifications
    $scope.$watch('prp.dpe.kwh', function(newVal, oldVal) {
        $scope.prp.dpe.classificationKWh = ServiceDPE.getKWHClassifiation(newVal);
    });
    $scope.$watch('prp.dpe.ges', function(newVal, oldVal) {
        $scope.prp.dpe.classificationGes = ServiceDPE.getGesClassifiation(newVal);
    });
};

