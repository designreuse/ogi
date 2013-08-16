function Ctrl($scope, $http, $rootScope, Page, ServiceObjectChecked) {
    $scope.properties = [];
    Page.setTitle("Liste des biens");

    $http.get('http://localhost:8080/ogi-ws/rest/property/').success(function (data) {
        $scope.properties = ServiceObjectChecked.addChecked(data, false);
    });


    $scope.type = function (property) {
        return property.category.code;
    }
    $scope.supprimer = function () {
        // Extract reference to selected items
        var refs = []
        angular.forEach($scope.selectedProperties(), function (o, key) {
            console.log(o.reference);
            refs.push(o.reference);
        }, refs);

        $http.delete('http://localhost:8080/ogi-ws/rest/property/',
            {"params": {
                "ref": refs
            }
            }).success(function (data) {

        });
    }

    /**
     * Return selected properties
     * @returns array
     */
    $scope.selectedProperties = function() {
        return ServiceObjectChecked.getChecked($scope.properties);
    }
}






