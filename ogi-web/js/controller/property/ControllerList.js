function ControllerList($scope, $http, Page, ServiceObjectChecked, ServiceAlert, ServiceConfiguration, $modal, $log, $dialogs) {
    Page.setTitle("Liste des biens");

    $scope.confirmDeletion = function(index) {
        $dialogs.confirm('Confirmation','Voulez-vous supprimer le bien ?')
            .result.then(function(btn){
                $http.delete(ServiceConfiguration.API_URL+"/rest/property/",
                    {"params": {
                        "ref": $scope.properties[index].reference
                    }
                    }).success(function (data) {
                        ServiceAlert.addSuccess("Le bien a été supprimé avec succès");
                        $scope.properties.splice(index, 1);
                    });
            });
    }


    $scope.properties = [];
    $http.get(ServiceConfiguration.API_URL+"/rest/property/").success(function (data) {
        $scope.properties = ServiceObjectChecked.addChecked(data, false);
    });


    /**
     * Return selected properties
     * @returns array
     */
    $scope.selectedProperties = function () {
        return ServiceObjectChecked.getChecked($scope.properties);
    }

    // ##### MODAL DELETE #####
    $scope.openModalDelete = function () {
        var selected = ServiceObjectChecked.getChecked($scope.properties);

        // If no items selected => msg
        if (selected.length == 0) {
            console.log("Merci de sélectionner des éléments");
            /*
             $dialog.messageBox("Information", "Merci de sélectionner des éléments", [
             {result: 'ok', label: 'OK', cssClass: 'btn-primary'}
             ]).open();*/
        } else {
            var modalInstance = $modal.open({
                templateUrl: 'modalPrpDelete.html',
                controller: ControllerModalDeleteInstance,
                resolve: {
                    selectedProperties: function () {
                        return selected;
                    }
                }
            });

            modalInstance.result.then(function () {
                $log.info('Modal closed');
            }, function () {
                $log.info('Modal dismissed');
            });
        }
    };

    // ##### MODAL ADD PRP #####
    $scope.openModalAddPrp = function () {
        var modalInstance = $modal.open({templateUrl: 'modalPrpAdd.html'});
    };

}

/** Controller for delete modal. Expose selected properties and delete it */
function ControllerModalDeleteInstance($scope, $modalInstance, ServiceConfiguration, ServiceAlert, $http, $log, selectedProperties) {
    $scope.selectedProperties = selectedProperties;
    $scope.delete = function() {
        // Extract reference to selected items
        var refs = [];
        angular.forEach($scope.selectedProperties, function (value, key) {
            refs.push(value.reference);
        }, refs);

        $http.delete(ServiceConfiguration.API_URL+"/rest/property/",
            {"params": {
                "ref": refs
            }
            }).success(function (data) {
                ServiceAlert.addSuccess("Les biens ont étés supprimés avec succès");
            }).
            error(function(data, status, headers, config) {
                ServiceAlert.addError("Une erreur est survenue "+data.exception);
            });

        $modalInstance.close();
    }

}
