angular.module('myApp.property').controller("ControllerPrpTabPartner",
function ($scope, Page, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log, Utils) {

    // Name must matches witch EnumPartner.code
    $scope.partners = [];
    $scope.partners.push({"name":"acimflo", "img":"img/LogoAcimflo.png", "exist": null, "lastRequest":null, "disable": false});
    $scope.partners.push({"name":"diaporama", "img":"img/LogoDiaporama.png", "exist": null,"lastRequest":null, "disable": false});
    $scope.partners.push({"name":"seloger", "img":"img/LogoSeLoger.png", "exist": null,"lastRequest":null, "disable": false});
    $scope.partners.push({"name":"leboncoin", "img":"img/LogoLeBonCoin.png", "exist": null,"lastRequest":null, "disable": false});
    $scope.partners.push({"name":"annoncesjaunes", "img":"img/LogoAnnoncesJaunes.png", "exist": null,"lastRequest":null, "disable": false});
    $scope.partners.push({"name":"bienici", "img":"img/LogoBienIci.png", "exist": null,"lastRequest":null, "disable": false});

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
                ServiceAlert.addSuccess("Le bien sera synchronisé sur le site spécifié lors de la prochaine exécution du traitement de synchronisation");
                getLastRequest(partner);
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
                ServiceAlert.addSuccess("Le bien sera synchronisé sur le site spécifié lors de la prochaine exécution du traitement de synchronisation");
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
});

