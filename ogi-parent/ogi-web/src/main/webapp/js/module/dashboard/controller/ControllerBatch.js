angular.module('myApp.common').controller("ControllerBatch",
function($scope, $http, Page, ServiceConfiguration) {
    Page.setTitle("Batch");

    var batches = {
        "seloger" : {"jobName":"jobPasserelle-seloger.com"},
        "acimflo" : {"jobName":"jobPasserelle-acimflo"},
        "diaporama" : {"jobName":"jobPasserelle-diaporama"}
    }

    // Read jobs status
    $scope.jobs = {};
    getExecutions("seloger");
    getExecutions("acimflo");

    function getExecutions(name) {
        $http.get(ServiceConfiguration.API_URL+"/rest/batch/",
            {"params": {"jobName": batches[name].jobName}}
            ).success(function (data) {
                $scope.jobs[name]= data;
        });
    }
});