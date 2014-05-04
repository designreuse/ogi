function ControllerPasserelleRecap($scope, $http, Page, ServiceConfiguration, Utils) {
    Page.setTitle("Récapitulatif des passerelles");


    // Reads jobs status
    $scope.passerelles = [];
    $http.get(ServiceConfiguration.API_URL+"/rest/synchronisation/")
        .success(function (data) {
            $scope.passerelles = data;
    });

    $scope.getPartner = function(arrayOfPartnerRequest, partner) {
        var a = [];
        if(arrayOfPartnerRequest) {
            a = arrayOfPartnerRequest.filter(function (r) {
                if(r) {
                    return r.partner == partner
                }
                return false;
            });
        }
        return Utils.isEmpty(a) ? {"requestType" : "NO"} : a[0];
    }
}
