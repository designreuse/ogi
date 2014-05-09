function ControllerPrpParent($scope, Page, $log, $http, $routeParams, ServiceConfiguration, Utils, ServiceAlert) {
    $scope.formCreate = true;

    // Top menu for active item
    $scope.addMenu = {
        "items" : [
            { "name" : "owner", "active" : false, "url" : "js/module/property/view/prpFormTabOwner.html"},
            { "name" : "prp", "active" : false, "url" : "js/module/property/view/prpFormTabGeneral.html"},
            { "name" : "desc", "active" : false, "url" : "js/module/property/view/prpFormTabDesc.html"},
            { "name" : "doc", "active" : false, "url" : "js/module/property/view/prpFormTabDocuments.html"},
            { "name" : "adminis", "active" : false, "url" : "js/module/property/view/prpFormTabAdministratif.html"},
            { "name" : "equipment", "active" : false, "url" : "js/module/property/view/formPrpTabGeneral.html"},
            { "name" : "diagnosis", "active" : false, "url" : "js/module/property/view/prpFormTabDiagnosis.html"},
            { "name" : "room", "active" : false, "url" : "js/module/property/view/prpFormTabRoom.html"},
            { "name" : "partner", "active" : false, "url" : "js/module/property/view/prpFormTabPartner.html"}
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
    $scope.addMenu.select("room");

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

    /**
     * This function is called by ControllerAdd and ControllerModify during save
     * Only prepare $scope.prp
     *
     * @param fn function to execute. Provide by caller. This function must save prp
     */
    $scope.updateTechnical = function(fn) {
        $scope.prp.mappingType= getMappingType($scope.prp.category.code);
        $scope.prp.address = $scope.saveData.address.isEmpty() ? null : $scope.saveData.address;
        // Send only techid
        $scope.prp.owners = $scope.saveData.ownersProperty.map(function(owner) {return owner.techid});

        fn();
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
        addressesOwner: [Object.create(address)],
        ownersProperty: []
    };
}