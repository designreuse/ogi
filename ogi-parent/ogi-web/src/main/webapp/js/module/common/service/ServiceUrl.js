angular.module('myApp.common').factory('ServiceUrl', function($location) {
    return {
        urlPropertyEdit : function(reference){
            return "/biens/modifier/"+reference;
        },
        urlOwner : function(techid){
            return "/proprietaires/modifier/"+techid;
        }
    }
});
