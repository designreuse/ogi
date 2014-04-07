function ControllerDashboard($scope, $http, Page, ServiceConfiguration) {
    Page.setTitle("Accueil");


    // Reads jobs status
    $scope.jobs = [];
    $http.get(ServiceConfiguration.API_URL+"/rest/batch/",
        {"params": {"jobName": "jobPasserelle-seloger.com"}}
        ).success(function (data) {
            $scope.jobs= data;
    });
}
