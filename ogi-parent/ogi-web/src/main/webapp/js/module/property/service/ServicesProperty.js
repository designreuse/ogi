angular.module('myApp.property').factory('ServiceDPE', function(Utils) {
    var dpeConfigKWH = [
        {min: 0, classification: "A"},
        {min: 51, classification: "B"},
        {min: 91, classification: "C"},
        {min: 151, classification: "D"},
        {min: 231, classification: "E"},
        {min: 331, classification: "F"},
        {min: 451, classification: "G"}
    ];
    var dpeConfigGes = [
        {min: 0, classification: "A"},
        {min: 6, classification: "B"},
        {min: 11, classification: "C"},
        {min: 21, classification: "D"},
        {min: 36, classification: "E"},
        {min: 56, classification: "F"},
        {min: 81, classification: "G"}
    ];

    var getClassification = function(tab, dpeValue) {
        if(Utils.isUndefinedOrNull(dpeValue)) {return "";}

        var conf = tab[0];
        for(var i = 0; i < tab.length; i++) {
            if (dpeValue >= tab[i].min) {
                conf = tab[i];
            }
        }
        return conf.classification;
    }
    return {
        getKWHClassifiation : function(dpeValue){
            return getClassification(dpeConfigKWH, dpeValue);
        },
        getGesClassifiation : function(dpeValue){
            return getClassification(dpeConfigGes, dpeValue);
        }
    }
});

angular.module('myApp.property').factory('ServiceLabel', function($http, ServiceConfiguration) {
    return {
        getLabels : function(type){
            return $http.get(ServiceConfiguration.API_URL+"/rest/label/"+type);
        }
    }
});