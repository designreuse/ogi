function AlertCtrl($scope, ServiceAlert) {
    $scope.alerts = ServiceAlert.getAlerts();
    $scope.closeAlert = function(index) {
        ServiceAlert.closeAlert(index);
    };

}




