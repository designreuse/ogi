function ControllerPasserelleRecap($scope, $http, Page, ServiceConfiguration, Utils) {
    Page.setTitle("Récapitulatif des passerelles");


    // Reads jobs status
    $scope.passerelles;
    $http.get(ServiceConfiguration.API_URL+"/rest/synchronisation/")
        .success(function (data) {
            $scope.passerelles = data;
            for(var key in data){
                var value = {};
                for(var i = 0; i < data[key].length ; i++) {
                    value[data[key][i].partner] =  data[key][i];
                }
                $scope.passerelles[key]= value;
            }
    });
}
