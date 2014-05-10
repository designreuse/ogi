function ControllerPrpParent($scope, $log, $http, $routeParams,
                             Page, ServiceConfiguration, ServiceObject, Utils, ServiceAlert) {
    $scope.formCreate = true;

    // Top menu for active item
    $scope.addMenu = {
        "items" : {
            "owner":    {"active" : false, "url" : "js/module/property/view/prpFormTabOwner.html"},
            "prp":      {"active" : false, "url" : "js/module/property/view/prpFormTabGeneral.html"},
            "desc":     {"active" : false, "url" : "js/module/property/view/prpFormTabDesc.html"},
            "doc":      {"active" : false, "url" : "js/module/property/view/prpFormTabDocuments.html"},
            "adminis":  {"active" : false, "url" : "js/module/property/view/prpFormTabAdministratif.html", "categToHide" : ["PLT"]},
            "equipment":{"active" : false, "url" : "js/module/property/view/formPrpTabGeneral.html"},
            "diagnosis":{"active" : false, "url" : "js/module/property/view/prpFormTabDiagnosis.html"},
            "room":     {"active" : false, "url" : "js/module/property/view/prpFormTabRoom.html", "categToHide" : ["PLT"]},
            "partner":  {"active" : false, "url" : "js/module/property/view/prpFormTabPartner.html"}
        },

        clean : function () {
            var items = this.items;
            Object.keys(items).map(function(value, index) {
                items[value].active = false;
            });
        },
        select : function (itemName) {
            this.clean();
            this.items[itemName].active = true;
        },
        getActive : function() {
            var activeKey = null;
            var items = this.items;
            Object.keys(items).map(function(value, index) {
                if(items[value].active === true) {
                    activeKey = value;
                }
            });
            return activeKey;
        }
    };
    $scope.addMenu.select("prp");

    /**
     * Indicate if tab must be display according to prp category
     * @param tab name of tab
     * @returns {boolean}
     */
    $scope.displayTab = function(tab) {
        // Retrieves the list of categories for which the tab is hidden
        var categToHide = $scope.addMenu.items[tab].categToHide;
        if($scope.prp.category && categToHide) {
            // Hide tab when prp category is in tab list
            return categToHide.filter(function(elt) { return elt == $scope.prp.category.code;}).length == 0;
        }
        // Default => display tab
        else { return true; }
    }

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