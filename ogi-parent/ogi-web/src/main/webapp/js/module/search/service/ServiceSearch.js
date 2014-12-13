angular.module('myApp.search').factory('ServiceSearch', function($http, ServiceConfiguration, $translate) {

    return {
        search : function(queryParams){
            return $http.get(ServiceConfiguration.API_URL+"/rest/search/", {"params": queryParams});
        },
        createSearchUrl: function (keyword, pageNumber, sort, filtersTerm, filtersRange) {
            var u = {};
            u.path = "/search";

            u.search = {}
            if(keyword) {
                u.search.keyword=keyword;
            }

            if(pageNumber) {
                u.search.p = pageNumber;
            }

            if(sort) {
                u.search.sort = sort.field;
                u.search.order = sort.order;
            }

            // Term
            if(filtersTerm) {
                for(var filterName in filtersTerm) {
                    var currentFilter = filtersTerm[filterName];
                    if(currentFilter.actif) {
                        u.search[currentFilter.paramUrl] = currentFilter.values;
                    }
                }
            }

            // Range
            if(filtersRange) {
                for(var filterName in filtersRange) {
                    var currentFilter = filtersRange[filterName];
                    for(var boundsFilterName in currentFilter) {
                        var boundsFilter = currentFilter[boundsFilterName];
                        if(boundsFilter.actif) {
                            u.search[boundsFilter.paramUrl] = boundsFilter.value;
                        }
                    }
                }

            }
            return u;
        },
        populateActivesFilters : function (keyword, filtersTerm, filtersRange) {
            var activesFilters = [];

            if(keyword) {
                activesFilters.push({"type" : "text", "name" : "keyword", "label" : keyword});
            }

            // Terms
            for(var filterName in filtersTerm) {
                var f = filtersTerm[filterName];
                // If filter is active => add to active filter
                if(f.actif) {
                    activesFilters.push({"type" : "term", "name" : filterName, "label" : f.values});
                }
            }

            // Range
            for(var filterName in filtersRange) {
                var currentFilter = filtersRange[filterName];

                // If one bound is active
                if(currentFilter.min.actif || currentFilter.max.actif) {
                    var translateKey = "search.filter.active."+filterName;
                    if(currentFilter.min.actif && currentFilter.max.actif) {
                        translateKey += ".min_max";
                    }
                    else if(currentFilter.min.actif && !currentFilter.max.actif) {
                        translateKey += ".min";
                    }
                    else if(!currentFilter.min.actif && currentFilter.max.actif) {
                        translateKey += ".max";
                    }
                    console.log(translateKey);

                    (function(filterName){
                    $translate(translateKey, {"min":currentFilter.min.value, "max":currentFilter.max.value}).then(function (t) {
                        activesFilters.push({"type" : "range", "name" : filterName, "label" : t});
                    });
                    })(filterName);
                }
            }

            return activesFilters;
        }
    }
});
