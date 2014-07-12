angular.module('myApp.common').factory('ServiceSearch', function($http, ServiceConfiguration) {

    return {
        search : function(keyword){
            var queryParams = {"keyword":keyword};
            return $http.get(ServiceConfiguration.API_URL+"/rest/search/", {"params": queryParams});
        }
    }
});
