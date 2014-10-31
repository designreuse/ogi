angular.module('myApp.passerelle').controller("ControllerPasserelleRecap",
function ($scope, $http, Page, ServiceConfiguration, Utils) {
    Page.setTitle("Récapitulatif des passerelles");

    // Reads jobs status
    $scope.passerelles;
    $scope.partnerPropertyCount;
    $http.get(ServiceConfiguration.API_URL+"/rest/synchronisation/")
        .success(function (data) {
            // Convert map of list into map of map passerelles["ref1"]["seloger"]
            $scope.passerelles = data.lastRequests;
            for(var key in data.lastRequests){
                var value = {};
                for(var i = 0; i < data.lastRequests[key].length ; i++) {
                    value[data.lastRequests[key][i].partner] =  data.lastRequests[key][i];
                }
                $scope.passerelles[key]= value;
            }

            $scope.partnerPropertyCount = data.partnerPropertyCount;
    });

    /**
     * In order to not overload table, display only some requests.
     * --> push
     * --> push_ack
     * @param prequest
     * @returns {boolean}
     */
    $scope.isDisplayable = function(prequest) {
        return prequest && (prequest.requestType == "push" || prequest.requestType == "push_ack");

    }

    $scope.total = function(partner) {
        var pTotal = [];
        if($scope.partnerPropertyCount) {
            pTotal = $scope.partnerPropertyCount.filter(function(elt) { return elt.partner == partner; });
        }
        if(pTotal.length) {
            return pTotal[0];
        }
        else {
            return null;
        }
    }
});
