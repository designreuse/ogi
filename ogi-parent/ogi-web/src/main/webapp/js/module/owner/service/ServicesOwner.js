angular.module('myApp.owner').factory('ServiceOwner', function($http, ServiceConfiguration) {
    return {
        get : function(criteria){
            return $http.get(ServiceConfiguration.API_URL+"/rest/owner/", {"params": criteria});
        }
    }
});