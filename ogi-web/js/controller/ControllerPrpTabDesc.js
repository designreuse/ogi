function ControllerPrpTabDesc($scope, Page, $routeParams, ServiceConfiguration, ServiceLabel, ServiceAlert, $http, $log ,$modal) {

    // Get type of current category. Run query only if promise of current type is resolved
    $scope.types = [];
    $scope.httpGetCurrentType.success(function() {
        $http.get(ServiceConfiguration.API_URL+"/rest/category/"+$scope.prp.category.code+"/types").success(function (data) {
            $scope.types = data;
            $scope.types.push("Autre");
        });
    });

    // Get roofs. Run query only if promise of current type is resolved
    $scope.roofs = [];
    $scope.httpGetCurrentType.success(function() {
        $http.get(ServiceConfiguration.API_URL+"/rest/label/ROOF").success(function (data) {
            $scope.roofs = data;
            $scope.roofs.push(labelOther);

            // to select the pointer must be identical
            $scope.prp.roof = ServiceLabel.getObject(data, $scope.prp.roof);
        });
    });

    // Get wall. Run query only if promise of current type is resolved
    $scope.walls = [];
    $scope.httpGetCurrentType.success(function() {
        $http.get(ServiceConfiguration.API_URL+"/rest/label/WALL").success(function (data) {
            $scope.walls = data;
            $scope.walls.push(labelOther);

            // to select the pointer must be identical
            $scope.prp.wall = ServiceLabel.getObject(data, $scope.prp.wall);
        });
    });

    // Get insulation. Run query only if promise of current type is resolved
    $scope.insulations = [];
    $scope.httpGetCurrentType.success(function() {
        $http.get(ServiceConfiguration.API_URL+"/rest/label/INSULATION").success(function (data) {
            $scope.insulations = data;
            $scope.insulations.push(labelOther);

            // to select the pointer must be identical
            $scope.prp.insulation = ServiceLabel.getObject(data, $scope.prp.insulation);
        });
    });

    // Get parkings. Run query only if promise of current type is resolved
    $scope.parkings = [];
    $scope.httpGetCurrentType.success(function() {
        $http.get(ServiceConfiguration.API_URL+"/rest/label/PARKING").success(function (data) {
            $scope.parkings = data;
            $scope.parkings.push(labelOther);

            // to select the pointer must be identical
            $scope.prp.parking = ServiceLabel.getObject(data, $scope.prp.parking);
        });
    });

    // Get all states
    $scope.states = [];
    $scope.states[0] = null;
    // First state is a NON state
    $scope.httpGetCurrentType.success(function() {
        $http.get(ServiceConfiguration.API_URL+"/rest/state/").success(function (data) {
            // Create array index by order
            for(var i = 0; i < data.length ; i++) {
                $scope.states[data[i].order] = data[i];
            }
        });
    });


    $scope.typeChange = function () {
        if($scope.prp.type == "Autre") {
            $scope.openModalType();
        }
    }
    $scope.roofChange = function () {
        if($scope.prp.roof != null && $scope.prp.roof.label == "Autre") {
            $scope.openModalRoof();
        }
    }
    $scope.wallChange = function () {
        if($scope.prp.wall != null && $scope.prp.wall.label == "Autre") {
            $scope.openModalWall();
        }
    }
    $scope.insulationChange = function () {
        if($scope.prp.insulation.label == "Autre") {
            $scope.openModalInsulation();
        }
    }
    $scope.parkingChange = function () {
        if($scope.prp.parking.label == "Autre") {
            $scope.openModalParking();
        }
    }

    // ##### MODAL TYPE #####
    $scope.openModalType = function () {
        var modalInstance = $modal.open({
            templateUrl: 'modalAddType.html',
            controller: ModalInstanceCtrl,
            resolve: {
                labels: function(){
                    return {
                        title:"Ajouter un type de bien",
                        placeholder:"Entrer un nouveau type"
                    };
                },
                currentElt: function () {
                    return "HSE";
                }
            }
        });

        modalInstance.result.then(function (param) {
            $log.info('Modal closed');
            $scope.types.push(param);
            $scope.prp.type = param;
        }, function () {
            $log.info('Modal dismissed');
            $scope.prp.type = "";
        });
    };

    // ##### MODAL ROOF #####
    $scope.openModalRoof = function () {
        var modalInstance = $modal.open({
            templateUrl: 'modalAddType.html',
            controller: ModalLabelInstanceCtrl,
            resolve: {
                labels: function(){
                    return {
                        title:"Ajouter une toiture",
                        placeholder:"Entrer une toiture"
                    };
                },
                currentElt: function () {
                    return "ROOF";
                }
            }
        });

        modalInstance.result.then(function (param) {
            $log.info('Modal closed');
            $scope.roofs.push(param);
            $scope.prp.roof = param;
        }, function () {
            $log.info('Modal dismissed');
            $scope.prp.roof = null;

        });
    };

    // ##### MODAL WALL #####
    $scope.openModalWall = function () {
        var modalInstance = $modal.open({
            templateUrl: 'modalAddType.html',
            controller: ModalLabelInstanceCtrl,
            resolve: {
                labels: function(){
                    return {
                        title:"Ajouter un mur",
                        placeholder:"Entrer un type de mur"
                    };
                },
                currentElt: function () {
                    return "WALL";
                }
            }
        });

        modalInstance.result.then(function (param) {
            $log.info('Modal closed');
            $scope.walls.push(param);
            $scope.prp.wall = param;
        }, function () {
            $log.info('Modal dismissed');
            $scope.prp.wall = "";
        });
    };

    // ##### MODAL INSULATION #####
    $scope.openModalInsulation = function () {
        var modalInstance = $modal.open({
            templateUrl: 'modalAddType.html',
            controller: ModalLabelInstanceCtrl,
            resolve: {
                labels: function(){
                    return {
                        title:"Ajouter une isolation",
                        placeholder:"Entrer un type d'isolation"
                    };
                },
                currentElt: function () {
                    return "INSULATION";
                }
            }
        });

        modalInstance.result.then(function (param) {
            $log.info('Modal closed');
            $scope.insulations.push(param);
            $scope.prp.insulation = param;
        }, function () {
            $log.info('Modal dismissed');
            $scope.prp.insulation = "";
        });
    };

    // ##### MODAL PARKING #####
    $scope.openModalParking = function () {
        var modalInstance = $modal.open({
            templateUrl: 'modalAddType.html',
            controller: ModalLabelInstanceCtrl,
            resolve: {
                labels: function(){
                    return {
                        title:"Ajouter un stationnement",
                        placeholder:"Entrer un type de stationnement"
                    };
                },
                currentElt: function () {
                    return "PARKING";
                }
            }
        });

        modalInstance.result.then(function (param) {
            $log.info('Modal closed');
            $scope.parkings.push(param);
            $scope.prp.parking = param;
        }, function () {
            $log.info('Modal dismissed');
            $scope.prp.parking = "";
        });
    };

}

var ModalInstanceCtrl = function ($scope, $modalInstance, ServiceConfiguration, $http, currentElt, labels) {
    $scope.newType = {"label" : null}; // Have to use object else in function ok value is not up to date
    $scope.labels =labels;

    $scope.ok = function () {
        var label = $scope.newType.label;

        $http.put(ServiceConfiguration.API_URL+"/rest/category/"+currentElt+"/types/"+label).success(function (data) {
            $modalInstance.close(label);
        })
        .error(function(data) {
            console.log("ERROR"+data);
        });
    };
};

var ModalLabelInstanceCtrl = function ($scope, $modalInstance, ServiceConfiguration, $http, currentElt, labels) {
    $scope.newType = {"label" : null}; // Have to use object else in function ok value is not up to date
    $scope.labels =labels;

    $scope.ok = function () {
        var label = {
            "techid": null,
            "type": currentElt,
            "label": $scope.newType.label
        }

        $http.post(ServiceConfiguration.API_URL+"/rest/label/", label).success(function (data) {
            $modalInstance.close(label);
        }).error(function(data) {
            console.log("ERROR"+data);
        });
    };
};

