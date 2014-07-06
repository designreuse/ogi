function ControllerPrpTabDocuments($scope, Page, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log) {
    $scope.availableFormats = {
        "docx" : {"title" : "Télécharger au format Word", "img": "img/logo-word.png"},
        "odt" : {"title" : "Télécharger au format Livre Office", "img": "img/logo-writer.png"},
        "pdf" : {"title" : "Télécharger au format PDF", "img": "img/logo-pdf.png"}
    }

    $scope.documents = [
        {
            "name" : "Fiche Vitrine",
            "type" : "vitrine",
            "availablePageSize" : ["A4", "A3"],
            "selectedPageSize" : "A4",
            "formats": ["pdf", "docx", "odt"]
        },

        {
            "name" : "Fiche Client",
            "type" : "client",
            "availablePageSize" : ["A4"],
            "selectedPageSize" : "A4",
            "formats": ["pdf"]
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
