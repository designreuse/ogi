angular.module('myApp.search').factory('ServiceSearch', function($http, ServiceConfiguration) {

    return {
        search : function(queryParams){
            return $http.get(ServiceConfiguration.API_URL+"/rest/search/", {"params": queryParams});
        },
        createSearchUrl: function (keyword, filters) {
            var u = {};
            u.path = "/search";

            u.search = {}
            if(keyword) {
                u.search.keyword=keyword;
            }

            if(filters) {
                for(var filterName in filters) {
                    var currentFilter = filters[filterName];
                    if(currentFilter.actif) {
                        // value is for "range" and values for "term"
                        u.search[currentFilter.paramUrl] = currentFilter.value || currentFilter.values;
                    }
                }
            }

            return u;
        }
    }
});
