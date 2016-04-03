function ControllerPrpParent($scope, $log, $http, $routeParams,
                             Page, ServiceConfiguration, ServiceObject, Utils, ServiceAlert, $dialogs) {
    $scope.formCreate = true;

    // Top menu for active item
    $scope.addMenu = {
        "items" : {
            "owner":    {"active" : false, "url" : "js/module/property/view/prpFormTabOwner.html"},
            "prp":      {"active" : false, "url" : "js/module/property/view/prpFormTabGeneral.html"},
            "desc":     {"active" : false, "url" : "js/module/property/view/prpFormTabDesc.html"},
            "doc":      {"active" : false, "url" : "js/module/property/view/prpFormTabDocuments.html"},
            "adminis":  {"active" : false, "url" : "js/module/property/view/prpFormTabAdministratif.html"},
            "equipment":{"active" : false, "url" : "js/module/property/view/formPrpTabGeneral.html"},
            "diagnosis":{"active" : false, "url" : "js/module/property/view/prpFormTabDiagnosis.html", "categToHide" : ["PLT"]},
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
    $scope.addMenu.select("owner");

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
            "PLT":"plot",
            "BSN":"business"
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

        // Send description only if value is not default
        var desc = {}
        for(var key in $scope.prp.descriptions) {
            if($scope.prp.descriptions[key].label != description[key].label) {
                desc[key] = $scope.prp.descriptions[key];
            }
        }
        $scope.prp.descriptions = desc;

        fn();
    };

  $scope.confirmDeletion = function(reference) {
    $dialogs.confirm('Confirmation','Voulez-vous supprimer le bien ?')
      .result.then(function(btn){
        $http.delete(ServiceConfiguration.API_URL+"/rest/property/",
          {"params": {
            "ref": reference
          }
          }).success(function (data) {
            ServiceAlert.addSuccess("Le bien a été supprimé avec succès");
            $scope.properties.splice(index, 1);
          });
      });
  };

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
};

/** Controller for delete modal. Expose selected properties and delete it */
angular.module('myApp.property').controller("ControllerModalDeleteInstance",
  function ($scope, $uibModalInstance, ServiceConfiguration, ServiceAlert, $http, $log, selectedProperties) {
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

      $uibModalInstance.close();
    }

  });