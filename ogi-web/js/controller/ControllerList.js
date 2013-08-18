function Ctrl($scope, $http, Page, ServiceObjectChecked, ServiceAlert, ServiceConfiguration) {
    Page.setTitle("Liste des biens");

    $scope.properties = [];
    $http.get(ServiceConfiguration.API_URL+"/rest/property/").success(function (data) {
        $scope.properties = ServiceObjectChecked.addChecked(data, false);
    });


    $scope.type = function (property) {
        return property.category.code;
    }
    $scope.supprimer = function () {
        // Extract reference to selected items
        var refs = [];
        angular.forEach($scope.selectedProperties(), function (o, key) {
            refs.push(o.reference);
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
    }

    /**
     * Return selected properties
     * @returns array
     */
    $scope.selectedProperties = function () {
        return ServiceObjectChecked.getChecked($scope.properties);
    }

}

var ModalCtrl = function ($scope, $dialog, ServiceObjectChecked) {
    $scope.open = function () {
        // If no items selected => msg
        if (ServiceObjectChecked.getChecked($scope.properties).length == 0) {
            $dialog.messageBox("Information", "Merci de sélectionner des éléments", [
                {result: 'ok', label: 'OK', cssClass: 'btn-primary'}
            ]).open();
        } else {
            $scope.shouldBeOpen = true;
        }
    };

    $scope.close = function () {
        $scope.shouldBeOpen = false;
    };

    $scope.opts = {
        backdropFade: true,
        dialogFade: true
    };

};
