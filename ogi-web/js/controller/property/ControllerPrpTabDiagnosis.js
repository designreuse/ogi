function ControllerPrpTabDiagnosis($scope, Page, $routeParams, ServiceConfiguration, ServiceDPE) {

    // Watch change for dpe values to update classification
    $scope.$watch('prp.dpe.kwh', function(newVal, oldVal) {
        $scope.prp.dpe.classificationKWh = ServiceDPE.getKWHClassifiation(newVal);
    });
    $scope.$watch('prp.dpe.ges', function(newVal, oldVal) {
        $scope.prp.dpe.classificationGes = ServiceDPE.getGesClassifiation(newVal);
    });
};

