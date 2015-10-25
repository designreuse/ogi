angular.module('myApp.property').controller("ControllerPrpTabDocuments",
function ($scope, Page, $routeParams, ServiceUrl, ServiceAlert, $http, $log) {
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
            "formats": ["pdf"]
        },

        {
            "name" : "Fiche Client",
            "type" : "client",
            "availablePageSize" : ["A4"],
            "selectedPageSize" : "A4",
            "formats": ["pdf"]
        }

    ]


    $scope.computeLink = function (doc, format) {
      var link = ServiceUrl.urlDocument($scope.prp.reference, doc.type, format, doc.selectedPageSize);
      window.location.href=link;
    }

    $scope.displayPageSize = function(doc) {
        return doc.availablePageSize.length > 1;
    }
});
