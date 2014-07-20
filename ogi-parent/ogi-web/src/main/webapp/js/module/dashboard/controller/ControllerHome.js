angular.module('myApp.common').controller("ControllerHome", ["$scope", "$http", "Page", "ServiceConfiguration",
    function($scope, $http, Page, ServiceConfiguration) {
    Page.setTitle("Accueil");

    // List expired mandates
    $scope.expiredMandates = {};
    $http.get(ServiceConfiguration.API_URL+"/rest/mandate/expired").success(function (data) {
        $scope.expiredMandates = data;
    });
}]);