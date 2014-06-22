function ControllerPrpTabDocuments($scope, Page, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log) {

    $scope.documents = [
        {
            "name" : "Fiche Vitrine",
            "type" : "vitrine",
            "availablePageSize" : ["A4", "A3"],
            "selectedPageSize" : "A4"
        },
        {
            "name" : "Fiche Classeur",
            "type" : "classeur",
            "availablePageSize" : ["A4"],
            "selectedPageSize" : "A4"
        }
    ]

    $scope.httpGetCurrentType.success(function() {
        $scope.linkShopFront = ServiceConfiguration.API_URL+"/rest/report/"+$scope.prp.reference;
    });

    $scope.computeLink = function (doc, format) {
        var link = $scope.linkShopFront+"?format="+format+"&type="+doc.type+"&pageSize="+doc.selectedPageSize;
        window.location.href=link;
    }

    $scope.displayPageSize = function(doc) {
        return doc.availablePageSize.length > 1;
    }
}
