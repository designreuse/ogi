angular.module('myApp.search').controller("ControllerSearch",
    function ControllerSearch($scope, $controller, $location, ServiceAlert, ServiceSearch, ServiceUrl, ServiceObject, Utils) {

        $scope.sortTmp = null;
        $scope.sortPossibilities = [
            /*
            {   "label" : "Prix de vente (du - cher au + cher)",
                "value": {'field':'price-sale', 'order':'ASC'}
            },
            {   "label" : "Prix de vente (du + cher au - cher)",
                "value": {'field':'price-sale', 'order':'DESC'}
            },
            {   "label" : "Prix de location (du - cher au + cher)",
                "value": {'field':'price-rent', 'order':'ASC'}
            },
            {   "label" : "Prix de location (du + cher au - cher)",
                "value": {'field':'price-rent', 'order':'DESC'}
            },
            */
            {   "label" : "Prix (du - cher au + cher)",
                "value": {'field':'price', 'order':'ASC'}
            },
            {   "label" : "Prix (du + cher au - cher)",
                "value": {'field':'price', 'order':'DESC'}
            },
            {   "label" : "Ville (de A à Z)",
                "value": {'field':'city', 'order':'ASC'}
            },
            {   "label" : "Ville (de Z à A)",
                "value": {'field':'city', 'order':'DESC'}
            },
            {   "label" : "Propriétaire (de A à Z)",
                "value": {'field':'owner', 'order':'ASC'}
            },
            {   "label" : "Propriétaire (de Z à A)",
                "value": {'field':'owner', 'order':'DESC'}
            }
        ];





        $scope.numberResultsPerPage = 9;
        $scope.pageNumber = 1;
        $scope.totalResults = 0;
        $scope.pageNumber = 1;
        $scope.sort = null;
        $scope.aggregations = {};
        $scope.property = {};
        $scope.activesFilters = [];

        $scope.keyword = "";
        $scope.filtersTerm={
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

        $scope.filtersRange = {
            "price" : {
                "min" : {
                    "paramUrl": "priceMin",
                    "actif": false,
                    "value" : null
                },
                "max" : {
                    "paramUrl": "priceMax",
                    "actif": false,
                    "value" : null
                }
            },
            "area" : {
                "min" : {
                    "paramUrl": "areaMin",
                    "actif": false,
                    "value" : null
                },
                "max" : {
                    "paramUrl": "areaMax",
                    "actif": false,
                    "value" : null
                }
            }
        }

        // Init filter according to url search parameters
        $scope.init = function() {
            $scope.keyword = $location.search().keyword;
            $scope.pageNumber = $location.search().p != null ? parseInt($location.search().p) :  1;

            if($location.search().sort) {
                var sort = $location.search().sort;
                var order = $location.search().order || "ASC";

                // Search sort according to url
                var o = $scope.sortPossibilities.filter(function (elt) {
                    if(elt.value.field == sort && elt.value.order == order) {
                       return true;
                    }
                    return false;
                });
                $scope.sortTmp = o[0] ;
                $scope.sort = o[0] ? o[0].value : null;
            }

            // TERM filters
            // iterate over url parameters
            for(var urlParamName in $location.search()) {
                // Search corresponding filter
                var filterKey = ServiceObject.getMapEntry($scope.filtersTerm, {"paramUrl":urlParamName}, ["paramUrl"]);

                if(filterKey) {
                    // Activate filter and setup its value
                    $scope.filtersTerm[filterKey].actif = true;
                    if(!Array.isArray($location.search()[urlParamName])) {
                        $scope.filtersTerm[filterKey].values = [$location.search()[urlParamName]];
                    }
                    else {
                        $scope.filtersTerm[filterKey].values = $location.search()[urlParamName];
                    }
                }
            }

            // RANGE filter
            // iterate over url parameters
            for(var urlParamName in $location.search()) {
                // Search corresponding filter
                for(var k in $scope.filtersRange) {
                    var currentFilterFirstLevel = $scope.filtersRange[k];
                    var filterKey = ServiceObject.getMapEntry(currentFilterFirstLevel, {"paramUrl":urlParamName}, ["paramUrl"]);
                    if(filterKey) {
                        currentFilterFirstLevel[filterKey].actif = true;
                        currentFilterFirstLevel[filterKey].value = $location.search()[urlParamName];
                        break;
                    }
                }
            }
        }


        $scope.addFilterTerm = function (name, value) {
            $scope.filtersTerm[name].actif=true;
            $scope.filtersTerm[name].values.push(value);

            $scope.changeUrl();
        }

        $scope.addFilterRange = function(name) {
            var f = $scope.filtersRange[name];

            // Activate min and max bounds if value is set
            if(f['min'].value) {
                f['min'].actif = true;
            }
            if(f['max'].value) {
                f['max'].actif = true;
            }

            $scope.changeUrl();
        }

        $scope.removeFilter = function (type, name) {
            if(type == "term") {
                $scope.filtersTerm[name].actif=false;
                $scope.filtersTerm[name].values = [];
            }
            else if(type == "range") {
                $scope.filtersRange[name].min.actif=false;
                $scope.filtersRange[name].max.actif=false;
                $scope.filtersRange[name].min.value = null;
                $scope.filtersRange[name].max.value = null;
            }
            else if(type == "text") {
                $scope.keyword = "";
            }
            $scope.changeUrl();
        }

        $scope.changeSort = function() {
            $scope.sort = $scope.sortTmp.value;
            $scope.changeUrl();
        }


        /* Create search url according to filters and redirect to this url */
        $scope.changeUrl = function() {
            var url = ServiceSearch.createSearchUrl($scope.keyword, $scope.pageNumber, $scope.sort, $scope.filtersTerm, $scope.filtersRange);
            console.log("URL");
            console.log(url);

            $location.path(url.path).search(url.search);
        }

        $scope.changePagination = function() {
            $scope.changeUrl();
        }


        // Extract values from actives filters and do search
        $scope.search = function() {
            var searchParams = {};
            searchParams.keyword = $scope.keyword;
            searchParams.p = $scope.pageNumber;

            if($scope.sort) {
                searchParams.sort = $scope.sort.field;
                searchParams.order = $scope.sort.order;
            }

            // Term
            for(var filterName in $scope.filtersTerm) {
                var currentFilter = $scope.filtersTerm[filterName];
                if(currentFilter.actif) {
                    searchParams[currentFilter.paramUrl] = currentFilter.values;
                }
            }

            // Range
            for(var filterName in $scope.filtersRange) {
                var currentFilter = $scope.filtersRange[filterName];
                for(var boundsFilterName in currentFilter) {
                    var boundsFilter = currentFilter[boundsFilterName];
                    if(boundsFilter.actif) {
                        searchParams[boundsFilter.paramUrl] = boundsFilter.value;
                    }
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



        $scope.$watch('filters', function(newValue, oldValue) {
            $scope.activesFilters = ServiceSearch.populateActivesFilters($scope.keyword, $scope.filtersTerm, $scope.filtersRange);
        });

        $scope.displayMySearch = function() {
            return $scope.activesFilters.length != 0;
        }


        $scope.init();
        $scope.search();

    });
