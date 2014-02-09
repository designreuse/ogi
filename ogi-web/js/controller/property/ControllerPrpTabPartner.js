function ControllerPrpTabPartner($scope, Page, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log, Utils) {

    $scope.partners = [];
    $scope.partners.push({"name":"acimflo", "img":"img/LogoAcimflo.png", "exist": null});
    $scope.partners.push({"name":"diaporama", "img":"img/LogoDiaporama.png", "exist": null});

    $scope.httpGetCurrentType.success(function() {
        $scope.partners.forEach(function(elt, index, array) {
            $http.get(ServiceConfiguration.API_URL+"/rest/synchronisation/"+elt.name+"/"+$scope.prp.reference)
                .success(function (data, status, headers) {
                    elt.exist=data.exist;
                });
        });
    });


    $scope.synchronize = function(partner) {
        var references = [$scope.prp.reference];
        $http.post(ServiceConfiguration.API_URL+"/rest/synchronisation/"+partner+"/", references)
            .success(function (data, status) {
                var errors = data.filter(function(elt) {return elt.code != "OK"});

                // Si au moins un retour en erreur, afficher les msg d'erreurs
                if(errors.length > 0 ) {
                    var msg = "Des erreurs se sont produites lors du traitement de synchrosation : <br />";
                    errors.forEach(function(elt) {
                        msg += elt.message +" <br />";
                    });
                    ServiceAlert.addError(msg);
                }
                // Si tous les retours sont en succès => 1 seul message OK
                else {
                    ServiceAlert.addSuccess("Synchronisation réalisée avec succès");
                    // Le bien est maintenant synchronisé. Dans tous les cas, il est présent dans le système du partenaire
                    // Positionne donc le boolean "exist" à true
                    $scope.partners.filter(function(elt) {return elt.name == partner}).forEach(function(elt) {
                        elt.exist = true;
                    })
                }
            });
    }

    $scope.delete = function(partner) {
        var references = [$scope.prp.reference];
        $http.delete(ServiceConfiguration.API_URL+"/rest/synchronisation/"+partner+"/", { "params" : {"ref" :references}})
            .success(function (data, status) {
                // Extract errors results
                var errors = data.filter(function(elt) {return elt.code != "OK"});

                // Si au moins un retour en erreur, afficher les msg d'erreurs
                if(errors.length > 0 ) {
                    var msg = "Des erreurs se sont produites lors de la suppression : <br />";
                    errors.forEach(function(elt) {
                        msg += elt.message +" <br />";
                    });
                    ServiceAlert.addError(msg);
                }
                // Si tous les retours sont en succès => 1 seul message OK
                else {
                    // Le bien a été supprimé du système du partenaire => change a valeur du boolean "exist" à false
                    $scope.partners.filter(function(elt) {return elt.name == partner}).forEach(function(elt) {
                        elt.exist = false;
                    })
                    ServiceAlert.addSuccess("Suppression réalisée avec succès");
                }

            });
    }

};

