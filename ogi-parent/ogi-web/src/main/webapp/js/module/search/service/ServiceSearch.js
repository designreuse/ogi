angular.module('myApp.search').factory('ServiceSearch', function($http, ServiceConfiguration) {

    return {
        search : function(queryParams){
            return $http.get(ServiceConfiguration.API_URL+"/rest/search/", {"params": queryParams});
        },
        createSearchUrl: function (keyword, filtersTerm, filtersRange) {
            var u = {};
            u.path = "/search";

            u.search = {}
            if(keyword) {
                u.search.keyword=keyword;
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
        populateActivesFilters : function (filtersTerm, filtersRange) {
            var activesFilters = [];

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
                    var label = "Entre "+currentFilter.min.value +" et "+ currentFilter.max.value;
                    activesFilters.push({"type" : "range", "name" : filterName, "label" : label});
                }

            }

            return activesFilters;
        }
    }
});
