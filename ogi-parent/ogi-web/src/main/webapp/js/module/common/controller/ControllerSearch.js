angular.module('myApp.common').controller("ControllerSearch",
function ControllerSearch($scope, $location, ServiceAlert, ServiceSearch, ServiceUrl) {
    $scope.keyword = "";

    $scope.submitSearch = function() {
        ServiceSearch.search($scope.keyword).success(function (data, status) {
            // If only one result => redirect to it
            if(data.total == 1) {
                var urlToRedirect = ServiceUrl.urlPropertyEdit(data.prp[0].reference);
                $location.url(urlToRedirect);
            }
            // else error ( list not yet implemented)
            else {
                ServiceAlert.addError("Aucun bien ne correspond à la référence "+$scope.keyword);
            }
        });
    }

});
