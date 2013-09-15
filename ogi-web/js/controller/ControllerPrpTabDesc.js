function ControllerPrpTabDesc($scope, Page, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log ,$modal) {

    // Get type of current category. Run query only if promise of current type is resolved
    $scope.types = [];
    $scope.httpGetCurrentType.success(function() {
        $http.get(ServiceConfiguration.API_URL+"/rest/category/"+$scope.currentType.code+"/types").success(function (data) {
            $scope.types = data;
            $scope.types.push("Autre");
        });
    });


    $scope.typeChange = function () {
        if($scope.prp.jsType == "Autre") {
            $scope.openModalType();
        }
    }

    // ##### MODAL TYPE #####
    $scope.openModalType = function () {
        var modalInstance = $modal.open({
            templateUrl: 'modalAddType.html',
            controller: ModalInstanceCtrl,
            resolve: {
                currentCategory: function () {
                    return "Maison";
                }
            }
        });

        modalInstance.result.then(function (param) {
            $log.info('Modal closed');
            $scope.types.push(param);
            $scope.prp.jsType = param;
        }, function () {
            $log.info('Modal dismissed');
            $scope.prp.jsType = "";
        });
    };

}

var ModalInstanceCtrl = function ($scope, $modalInstance, ServiceConfiguration, $http, currentCategory) {
    $scope.currentCategory = currentCategory;
    $scope.newType = {"label" : null}; // Have to use object else in function ok value is not up to date

    $scope.ok = function () {
        var label = $scope.newType.label;

        $http.put(ServiceConfiguration.API_URL+"/rest/category/HSE/types/"+label).success(function (data) {
            console.log(data);
            $modalInstance.close(label);
        })
        .error(function(data) {
            console.log("ERRROOOOOOOOOOOR"+data);
        });
    };
};
