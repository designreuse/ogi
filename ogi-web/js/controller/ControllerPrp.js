function ControllerPrpParent($scope, Page, $log, $http, ServiceConfiguration) {
    // Top menu for active item
    $scope.addMenu = {
        "items" : [
            { "name" : "owner", "active" : false, "url" : "js/views/formPrpTabGeneral.html"},
            { "name" : "prp", "active" : false, "url" : "js/views/formPrpTabGeneral.html"},
            { "name" : "desc", "active" : false, "url" : "js/views/formPrpTabDesc.html"},
            { "name" : "equipment", "active" : false, "url" : "js/views/formPrpTabGeneral.html"},
            { "name" : "adminis", "active" : false, "url" : "js/views/formPrpTabGeneral.html"},
            { "name" : "diagnosis", "active" : false, "url" : "js/views/formPrpTabGeneral.html"},
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

    $scope.update = function() {
        $scope.prp.mappingType= "HSE";
        $http.post(ServiceConfiguration.API_URL+"/rest/property/", $scope.prp)
            .success(function (data, status) {
                $scope.prp = new PropertyJS(data);
            })
            .error(function (data, status) {
                console.error(data);
            });
    }

    $scope.tempReference = Math.random().toString(36).substring(7);
    $log.debug("tempReference="+ $scope.tempReference);

    // Data to save between children controller
    $scope.saveData = {
        stateOrder: 0,
        roof: null
    };

    $scope.addMenu.select("desc");

}