function ControllerPrp($scope, Page, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log) {
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
        console.log($scope.prp.wall.label);
    }

    // Current type to add (code + label)
    $scope.currentType = {};
    // Get information about current type
    $scope.httpGetCurrentType = $http.get(ServiceConfiguration.API_URL+"/rest/category/"+$routeParams.type).success(function (data) {
        $scope.currentType = data;
        Page.setTitle("Ajouter un bien : "+data.label);
    });

    $scope.addMenu.select("desc");
    $scope.tempReference = Math.random().toString(36).substring(7);
    $log.log("tempReference="+ $scope.tempReference);

    $scope.prp = new PropertyJS({});

    // Data to save between children controller
    $scope.saveData = {stateOrder: 0};
}



