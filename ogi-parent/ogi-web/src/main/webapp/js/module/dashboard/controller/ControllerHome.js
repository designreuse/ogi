angular.module('myApp.common').controller("ControllerHome", ["$scope", "$http", "Page", "ServiceConfiguration",
    function($scope, $http, Page, ServiceConfiguration) {
    Page.setTitle("Accueil");

    // List expired mandates
    $scope.expiredMandates = {};
    $http.get(ServiceConfiguration.API_URL+"/rest/mandate/expired").success(function (data) {
        $scope.expiredMandates = data;
    });

    $scope.numberExpiredMandates = function() {
    return ($scope.expiredMandates["EXPIRED"] ? $scope.expiredMandates["EXPIRED"].length : 0) +
        ($scope.expiredMandates["SOON_EXPIRED"] ? $scope.expiredMandates["SOON_EXPIRED"].length : 0);
    }
}]);