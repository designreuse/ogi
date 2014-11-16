angular.module('myApp.search').factory('ServiceSearch', function($http, ServiceConfiguration) {

    return {
        search : function(keyword){
            var queryParams = {"keyword":keyword};
            return $http.get(ServiceConfiguration.API_URL+"/rest/search/", {"params": queryParams});
        },
        createSearchUrl: function (keyword) {
            var u = {};
            u.path = "/search";

            u.search = {}
            u.search.keyword=keyword;

            return u;
        }
    }
});
