function ControllerPrp($scope, Page, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log) {
    Page.setTitle("Ajouter un bien : "+$routeParams.type);

    // Top menu for active item
    $scope.addMenu = {
        "items" : [
            { "name" : "owner", "active" : false, "url" : "js/views/formPrp.html"},
            { "name" : "prp", "active" : false, "url" : "js/views/formPrp.html"},
            { "name" : "desc", "active" : false, "url" : "js/views/formDesc.html"},
            { "name" : "equipment", "active" : false, "url" : "js/views/formPrp.html"},
            { "name" : "adminis", "active" : false, "url" : "js/views/formPrp.html"},
            { "name" : "diagnosis", "active" : false, "url" : "js/views/formPrp.html"},
            { "name" : "room", "active" : false, "url" : "js/views/formPrp.html"}
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

    $scope.addMenu.select("prp");
    $scope.tempReference = Math.random().toString(36).substring(7);
    $log.log("tempReference="+ $scope.tempReference);

    $scope.prp = {
        address : Object.create(address),
        photos : []
    };
}



