function ControllerPrpParent($scope, Page, $log, $http, ServiceConfiguration, Utils, ServiceAlert) {
    // Top menu for active item
    $scope.addMenu = {
        "items" : [
            { "name" : "owner", "active" : false, "url" : "js/views/property/prpFormTabOwner.html"},
            { "name" : "prp", "active" : false, "url" : "js/views/property/prpFormTabGeneral.html"},
            { "name" : "desc", "active" : false, "url" : "js/views/property/prpFormTabDesc.html"},
            { "name" : "doc", "active" : false, "url" : "js/views/property/prpFormTabDocuments.html"},
            { "name" : "adminis", "active" : false, "url" : "js/views/property/prpFormTabAdministratif.html"},
            { "name" : "equipment", "active" : false, "url" : "js/views/formPrpTabGeneral.html"},
            { "name" : "diagnosis", "active" : false, "url" : "js/views/property/prpFormTabDiagnosis.html"},
            { "name" : "room", "active" : false, "url" : "js/views/formPrpTabGeneral.html"}
        ],

        clean : function () {
            angular.forEach(this.items, function (value, key) {
                value.active = false;
            });
        },
        select : function (itemName) {
            this.clean();
            angular.forEach(this.items, function (value, key) {
                if(value.name == itemName) {
                    value.active = true;
                }
            });
        },
        getActive : function() {
            var itemActive = null;
            angular.forEach(this.items, function (value, key) {
                if(value.active === true) {
                    itemActive = value;
                }
            });
            return itemActive;
        }
    };
    $scope.addMenu.select("diagnosis");

    /**
     * Le flux json doit contenir le type du bien car en java, il y a un héritage. Il faut donc connaitre la classe
     * à instancier
     * @param categCode code de la catégorie du bien
     * @returns string
     */
    function getMappingType(categCode) {
        var mt = {
            "HSE":"liveable",
            "APT":"liveable",
            "PLT":"plot"
        }
        return mt[categCode];
    }

    $scope.update = function() {
        $scope.prp.mappingType= getMappingType($scope.prp.category.code);
        $scope.prp.address = $scope.saveData.address.isEmpty() ? null : $scope.saveData.address;

        // Create or modify property
        $http.post(ServiceConfiguration.API_URL+"/rest/property/", $scope.prp)
            .success(function (data, status) {
                ServiceAlert.addSuccess("Enregistrement du bien OK");
                $scope.prp = new PropertyJS(data);
            });
    }

    // Generating temp reference for property (necessary for store uploaded files)
    $scope.tempReference = Math.random().toString(36).substring(7);
    $log.debug("tempReference="+ $scope.tempReference);

    // Data to save between children controller
    $scope.prp = new PropertyJS({});
    $scope.saveData = {
        stateOrder: 0,
        roof:null,
        wall:null,
        insulation:null,
        parking:null,
        type:null,
        address: Object.create(address),
        addressesOwner: [Object.create(address)]
    };

    // Ce controler parent stocke également le propriétaire car le bouton de validation n'est pas dans le scope du ControllerOwner
    $scope.owner = null;
    // Il est impossible de réallouer une variable dans les controller fils. Utilise donc une fonction
    $scope.setOwner = function(o) {
        $scope.owner = o;
    }

}