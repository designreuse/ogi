function ControllerOwnerParent($scope, $injector, ServiceConfiguration, ServiceAlert, $http, $log, Utils) {
  $scope.owner = null;
  $scope.setOwner = function (o) {
    $scope.owner = o;
  }

  $scope.saveData = {
    addressesOwner: [Object.create(address)]
  };

  $scope.updateTechnical = function (fn) {
    // If owner is defined => create / modify it
    if (!Utils.isUndefinedOrNull($scope.owner)) {
      // Il ne faut pas envoyer une adresse vide sinon les contraintes d'intégrités ne sont pas respectées
      $scope.owner.addresses = $scope.saveData.addressesOwner.length == 0 || $scope.saveData.addressesOwner[0].isEmpty() ? [] : $scope.saveData.addressesOwner;
      fn();
    }
  }

  $scope.callbackSuccess = function (data, status) {
    ServiceAlert.addSuccess("Enregistrement du propriétaire OK");
  };
  $scope.callbackError = function (data, status) {
    ServiceAlert.addError("Erreur lors de l'enregistrement du propriétaire : " + ServiceAlert.formatErrors(data.errors));
  }


  $scope.openCalendar = function ($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.opened = true;
  };

  $scope.dateOptions = {
    'year-format': "'yyyy'",
    'starting-day': 1
  };
}