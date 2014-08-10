angular.module('myApp.common').controller("AlertCtrl",
function ($scope, ServiceAlert) {
    $scope.alerts = ServiceAlert.getAlerts();
    $scope.closeAlert = function(index) {
        ServiceAlert.closeAlert(index);
    };

});
