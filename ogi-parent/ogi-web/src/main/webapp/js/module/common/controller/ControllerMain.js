angular.module('myApp.common').controller("MainCtrl",
function ($scope, Page, Utils, ServiceSearch, $location) {
    $scope.page = Page;
    $scope.utils = Utils;

    $scope.keyword = "";
    $scope.submitSearch = function() {
        // Compute url o search and redirect
        var url = ServiceSearch.createSearchUrl($scope.keyword);
        $location.path(url.path).search(url.search);
    }


});