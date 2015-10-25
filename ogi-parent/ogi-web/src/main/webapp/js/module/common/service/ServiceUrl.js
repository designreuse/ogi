angular.module('myApp.common').factory('ServiceUrl', function(ServiceConfiguration) {
    return {
        urlPropertyEdit : function(reference){
            return "#/biens/modifier/"+reference;
        },
        urlOwner : function(techid){
            return "#/proprietaires/modifier/"+techid;
        },
        urlDocument: function(reference, documentType, format, pageSize) {
          return ServiceConfiguration.API_URL+"/rest/report/"+reference+"?format="+format+"&type="+documentType+"&pageSize="+pageSize;
        }
    }
});
