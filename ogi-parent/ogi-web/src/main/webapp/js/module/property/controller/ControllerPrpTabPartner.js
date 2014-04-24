function ControllerPrpTabPartner($scope, Page, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log, Utils) {

    // Name must matches witch EnumPartner.code
    $scope.partners = [];
    $scope.partners.push({"name":"acimflo", "img":"img/LogoAcimflo.png", "exist": null, "lastRequest":null, "disable": false});
    $scope.partners.push({"name":"diaporama", "img":"img/LogoDiaporama.png", "exist": null,"lastRequest":null, "disable": false});
    $scope.partners.push({"name":"seloger", "img":"img/LogoSeLoger.png", "exist": null,"lastRequest":null, "disable": false});

    $scope.httpGetCurrentType.success(function() {
        $scope.partners.forEach(function(elt, index, array) {
            getLastRequest(elt);
        });
    });


    //Send a request to synchronise (create/update) current real property
    $scope.synchronize = function(partner) {
        partner.disable = true;
        var references = [$scope.prp.reference];
        $http.post(ServiceConfiguration.API_URL+"/rest/synchronisation/"+partner.name+"/", references)
            .success(function (data, status) {

                // 3 status for a request
                // Webservice deal with many real property so it's why it return list
                var errors = data.filter(function(elt) {return elt.code == "KO"});
                var success = data.filter(function(elt) {return elt.code == "OK"});
                var wait = data.filter(function(elt) {return elt.code == "WAIT"});

                if(errors.length > 0 ) {
                    var msg = "Des erreurs se sont produites lors du traitement de synchrosation : <br />";
                    errors.forEach(function(elt) {
                        msg += elt.message +" <br />";
                    });
                    ServiceAlert.addError(msg);
                }
                else if(success.length > 0) {
                    ServiceAlert.addSuccess("Synchronisation réalisée avec succès");
                    getLastRequest(partner);
                }
                else if(wait.length > 0) {
                    ServiceAlert.addSuccess("Le bien sera synchronisé sur le site spécifié lors de la prochaine exécution du traitement de synchronisation");
                    getLastRequest(partner);
                }
            }).finally(function() {
                partner.disable = false;
            });
    }

    //Send a request to delete current real property
    $scope.delete = function(partner) {
        partner.disable = true;
        var references = [$scope.prp.reference];
        $http.delete(ServiceConfiguration.API_URL+"/rest/synchronisation/"+partner.name+"/", { "params" : {"ref" :references}})
            .success(function (data, status) {
                var errors = data.filter(function(elt) {return elt.code == "KO"});
                var success = data.filter(function(elt) {return elt.code == "OK"});
                var wait = data.filter(function(elt) {return elt.code == "WAIT"});

                if(errors.length > 0 ) {
                    var msg = "Des erreurs se sont produites lors de la suppression : <br />";
                    errors.forEach(function(elt) {
                        msg += elt.message +" <br />";
                    });
                    ServiceAlert.addError(msg);
                }
                else if(success.length > 0) {
                    ServiceAlert.addSuccess("Suppression réalisée avec succès");
                }
                else if(wait.length > 0) {
                    ServiceAlert.addSuccess("Le bien sera synchronisé sur le site spécifié lors de la prochaine exécution du traitement de synchronisation");
                }

                getLastRequest(partner);

            }).finally(function() {
                partner.disable = false;
            });
    }


    var getLastRequest = function(partner) {
        partner.disable = true;
        $http.get(ServiceConfiguration.API_URL+"/rest/synchronisation/"+partner.name+"/"+$scope.prp.reference)
            .success(function (data, status, headers) {
                partner.exist=data.exist;
                partner.lastRequest = data.lastRequest;
            }).finally(function() {
                partner.disable = false;
            });
    }
};

