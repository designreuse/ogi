angular.module('myApp.search').controller("ControllerSearch",
    function ControllerSearch($scope, $location, ServiceAlert, ServiceSearch, ServiceUrl, ServiceObject) {
        $scope.totalResults = 0;
        $scope.aggregations = {};
        $scope.property = {};
        $scope.activesFilters = [];

        $scope.keyword = "";
        $scope.filters={
            "modes" : {
                "paramUrl": "modes",
                "actif": false,
                "values" : [],
                "type" : "term"
            },
            "categories" : {
                "paramUrl": "categories",
                "actif": false,
                "values" : [],
                "type" : "term"
            },
            "cities" : {
                "paramUrl": "cities",
                "actif": false,
                "values" : [],
                "type" : "term"
            },
            "price-min" : {
                "paramUrl": "priceMin",
                "actif": false,
                "value" : null,
                "type" : "range"
            },
            "price-max" : {
                "paramUrl": "priceMin",
                "actif": false,
                "value" : null,
                "type" : "range"
            },
            "area-min" : {
                "paramUrl": "areaMin",
                "actif": false,
                "value" : null,
                "type" : "range"
            },
            "area-max" : {
                "paramUrl": "areaMin",
                "actif": false,
                "value" : null,
                "type" : "range"
            }

        };

        // Init filter according to url search parameters
        $scope.init = function() {
            $scope.keyword = $location.search().keyword;

            // iterate over url parameters
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
        }


        $scope.addFilterTerm = function (filterType, value) {
            $scope.filters[filterType].actif=true;
            $scope.filters[filterType].values.push(value);

            $scope.changeUrl();
        }

        $scope.removeFilterTerm = function (name) {
            $scope.filters[name].actif=false;
            $scope.filters[name].values = [];

            $scope.changeUrl();
        }

        $scope.addFilterRange = function(filterName) {
            // Activate min and max bounds if value is set
            if($scope.filters[filterName+'-min'].value) {
                $scope.filters[filterName+'-min'].actif = true;
            }
            if($scope.filters[filterName+'-max'].value) {
                $scope.filters[filterName+'-max'].actif = true;
            }

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
                $scope.totalResults = data.totalResults;
            });
        }


        /**
         * Create actives filters array according to $scope.filters state
         * @returns {Array}
         */
        $scope.populateActivesFilters = function () {
            var filters = $scope.filters;
            $scope.activesFilters = [];

            for(var filterName in filters) {
                var f = filters[filterName];
                // If filter is active => add to active filter
                if(f.actif) {
                    if(f.type == "range") {

                    }
                    else {
                        $scope.activesFilters.push({"name" : filterName, "values" : f.values});
                    }


                }
            }
            return $scope.activesFilters;
        }



        $scope.$watch('filters', function(newValue, oldValue) {
            $scope.populateActivesFilters();
        });


        $scope.init();
        $scope.search();
    });
