angular.module('myApp.owner').controller("ControllerOwnerList",
function ($scope, $http, Page, ServiceAlert, ServiceConfiguration, $modal, $log, $dialogs, ServiceOwner) {
    Page.setTitle("Liste des propriétaires");

    $scope.owners = [];

    $scope.delete = function(index) {
        $dialogs.confirm('Confirmation','Voulez-vous supprimer le propriétaire ?')
            .result.then(function(btn){
                $http.delete(ServiceConfiguration.API_URL+"/rest/owner/",
                    {"params": {
                        "ref": $scope.owners[index].techid
                    }
                    }).success(function (data) {
                        ServiceAlert.addSuccess("Le propriétaire a été supprimé avec succès");
                        $scope.owners.splice(index, 1);
                    });
            });
    }


    // ###### List owners with pagination ######
    $scope.filterCriteria = {
        pageNumber: 1,
        itemNumberPerPage : 20,
        sortDir: 'asc',
        sortBy: 'name'
    };
    $scope.totalItems = 0;


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

    $scope.sort = function (sortedBy, sortDir) {
        $scope.filterCriteria.sortDir = sortDir;
        $scope.filterCriteria.sortBy = sortedBy;
        $scope.filterCriteria.pageNumber = 1;
        $scope.fetchResult();
    };

    // Run query to fetch data
    $scope.fetchResult();
});
