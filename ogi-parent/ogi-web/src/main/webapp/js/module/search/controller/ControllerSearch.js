angular.module('myApp.search').controller("ControllerSearch",
    function ControllerSearch($scope, $location, ServiceAlert, ServiceSearch, ServiceUrl, ServiceObject) {
        $scope.aggregations = {};
        $scope.property = {};

        $scope.keyword = "";
        $scope.filters={
            "modes" : {
                "paramUrl": "modes",
                "actif": false,
                "values" : []
            },
            "categories" : {
                "paramUrl": "categories",
                "actif": false,
                "values" : []
            },
            "cities" : {
                "paramUrl": "cities",
                "actif": false,
                "values" : []
            }

        };

        // Init filter according to url search parameters
        $scope.init = function() {
            $scope.keyword = $location.search().keyword;

            for(var urlParamName in $location.search()) {
                // Search corresponding filter
                var filterKey = ServiceObject.getMapEntry($scope.filters, {"paramUrl":urlParamName}, ["paramUrl"]);
                if(filterKey) {
                    // Activate filter and setup its value
                    $scope.filters[filterKey].actif = true;
                    if(!Array.isArray($location.search()[urlParamName])) {
                        $scope.filters[filterKey].values = [$location.search()[urlParamName]];
                    }
                    else {
                        $scope.filters[filterKey].values = $location.search()[urlParamName];
                    }
                }
            }

            console.log("FILTERS");
            console.log($scope.filters);
        }


        $scope.addFilterTerm = function (filterType, value) {
            $scope.filters[filterType].actif=true;
            $scope.filters[filterType].values.push(value);

            $scope.changeUrl();
        }

        /* Create search url according to filters */
        $scope.changeUrl = function() {
            var url = ServiceSearch.createSearchUrl($scope.keyword, $scope.filters);

            console.log("URL");
            console.log(url);

            $location.path(url.path).search(url.search);
        }


        // Get params from url and do search
        $scope.search = function() {
            var searchParams = {};
            searchParams.keyword = $scope.keyword;

            for(var filterName in $scope.filters) {
                var currentFilter = $scope.filters[filterName];
                if(currentFilter.actif) {
                    searchParams[currentFilter.paramUrl] = currentFilter.values;
                }
            }
            console.log("SEARCH");
            console.log(searchParams);

            ServiceSearch.search(searchParams).success(function (data, status) {
                $scope.aggregations = data.aggregations;
                $scope.property = data.property;
            });
        }
        $scope.init();
        $scope.search();
    });
