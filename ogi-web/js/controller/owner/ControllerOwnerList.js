function ControllerOwnerList($scope, $http, Page, ServiceAlert, ServiceConfiguration, $modal, $log, $dialogs) {
    Page.setTitle("Liste des propriétaires");

    $scope.owners = [];
    $http.get(ServiceConfiguration.API_URL+"/rest/owner/").success(function (data) {
        $scope.owners = data;
    });


    $scope.delete = function(index) {
        $dialogs.confirm('Confirmation','Voulez-vous supprimer le propriétaire ?')
            .result.then(function(btn){
                $http.delete(ServiceConfiguration.API_URL+"/rest/owner/",
                    {"params": {
                        "ref": $scope.owners[index].techid
                    }
                    }).success(function (data) {
                        ServiceAlert.addSuccess("Le propriétaire a été supprimé avec succès");
                        $scope.owners.splice(index, 1);
                    });
            });
    }
}
