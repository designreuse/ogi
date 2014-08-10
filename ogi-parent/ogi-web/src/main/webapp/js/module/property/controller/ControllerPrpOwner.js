angular.module('myApp.property').controller("ControllerPrpTabOwner",
function ($scope, $injector, $routeParams, ServiceConfiguration, ServiceAlert,
                               $http, ServiceObject, Utils, $location, ServiceOwner) {
    $scope.owners = [];         // All owners

    $scope.filterCriteria = {
        pageNumber: 1,
        itemNumberPerPage : 20,
        sortDir: 'asc',
        sortBy: 'name'
    };

    $scope.sort = function (sortedBy, sortDir) {
        $scope.filterCriteria.sortDir = sortDir;
        $scope.filterCriteria.sortBy = sortedBy;
        $scope.filterCriteria.pageNumber = 1;
        $scope.fetchResult();
    };

    $scope.selectPage = function() {
        $scope.fetchResult();
    }

    //The function that is responsible of fetching the result from the server and setting the grid to the new result
    $scope.fetchResult = function () {
        ServiceOwner.get($scope.filterCriteria)
            .success(function (data) {
                $scope.owners = data.items;
                $scope.totalItems = data.total;
            });
    };
    // Run query to fetch data
    $scope.fetchResult();



    $scope.httpGetCurrentType.success(function() {
        var readOwnerToAssociate;
        // Lecture du propriétaire dont le techid est dans l'url. Cela signifie que l'utilisateur revient de la page
        // "création de propriétaire". Le propriétaire a été créé mais pas encore lié au bien car le bien n'existe potentiellement
        // pas
        if($location.search().ownerTechid) {
            readOwnerToAssociate = $http.get(ServiceConfiguration.API_URL+"/rest/owner/"+$location.search().ownerTechid)
                .success(function (data, status, headers) {
                    addToPrpList(data);
                });
        }

    });

    /**
     * When user click on IHM, get owner and associate with prp
     * @param techid
     */
    $scope.addToProperty = function (techid) {
        var ownerToAdd = ServiceObject.getObject($scope.owners, {"techid" : techid}, ["techid"]);
        addToPrpList(ownerToAdd);
    }

    function addToPrpList(owner) {
        var o = ServiceObject.getObject($scope.saveData.ownersProperty, {"techid" : owner.techid}, ["techid"]);
        if(!o) {
            $scope.saveData.ownersProperty.push(owner);
        }
    }

    $scope.deleteToProperty = function (techid) {
        var ownerToDelete = ServiceObject.getObject($scope.saveData.ownersProperty, {"techid" : techid}, ["techid"]);
        var index = $scope.saveData.ownersProperty.indexOf(ownerToDelete);
        if (index > -1) {
            $scope.saveData.ownersProperty.splice(index, 1);
        }
        /*
        $http.delete(ServiceConfiguration.API_URL+"/rest/owner/"+ownerToDelete.techid+"/property/"+$routeParams.prpRef, null)
            .success(function (data, status) {

                ServiceAlert.addSuccess("Suppression de la liaison entre le propriétaire et le bien effectuée");
            })
            .error(function (data, status) {
                ServiceAlert.addError("Errreur lors de la suppression de la liaison entre le propriétaire et le bien : "+data.message);
            });
            */
    }

    $scope.displayZoneOwnerLink = function() {
        return $scope.saveData.ownersProperty.length > 0;
    }
});