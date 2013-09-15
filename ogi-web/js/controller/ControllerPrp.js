function ControllerPrp($scope, Page, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log) {
    Page.setTitle("Ajouter un bien : "+$routeParams.type);

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

    $scope.addMenu.select("desc");
    $scope.tempReference = Math.random().toString(36).substring(7);
    $log.log("tempReference="+ $scope.tempReference);

    $scope.prp = new PropertyJS({});
}



