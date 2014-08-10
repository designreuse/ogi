angular.module('myApp.common').controller("ControllerDashboard",
function($scope, $http, Page, ServiceConfiguration) {
    Page.setTitle("Batch");

    // Read jobs status
    $scope.jobs = [];
    $http.get(ServiceConfiguration.API_URL+"/rest/batch/",
        {"params": {"jobName": "jobPasserelle-seloger.com"}}
        ).success(function (data) {
            $scope.jobs= data;
    });
});