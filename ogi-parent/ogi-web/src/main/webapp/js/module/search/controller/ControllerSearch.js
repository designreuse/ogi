angular.module('myApp.search').controller("ControllerSearch",
    function ControllerSearch($scope, $location, ServiceAlert, ServiceSearch, ServiceUrl) {
        $scope.aggregations = {};
        $scope.property = {};


        $scope.addSearchParam = function (filterType, value) {
            console.log(value);
        }

        $scope.search = function() {
            var searchParams = {};
            searchParams.keyword = $location.search().keyword;

            ServiceSearch.search(searchParams).success(function (data, status) {
                console.log(data);
                $scope.aggregations = data.aggregations;
                $scope.property = data.property;
            });
        }


        $scope.search();
});
