function ControllerPasserelleRecap($scope, $http, Page, ServiceConfiguration, Utils) {
    Page.setTitle("Récapitulatif des passerelles");


    // Reads jobs status
    $scope.passerelles;
    $http.get(ServiceConfiguration.API_URL+"/rest/synchronisation/")
        .success(function (data) {
            // Convert map of list into map of map passerelles["ref1"]["seloger"]
            $scope.passerelles = data;
            for(var key in data){
                var value = {};
                for(var i = 0; i < data[key].length ; i++) {
                    value[data[key][i].partner] =  data[key][i];
                }
                $scope.passerelles[key]= value;
            }
    });

    /**
     * In order to not overload table, display only some requests.
     * --> push
     * --> push_ack
     * @param prequest
     * @returns {boolean}
     */
    $scope.isDisplayable = function(prequest) {
        return prequest && (prequest.requestType == "push" ||prequest.requestType == "push_ack");

    }
}
