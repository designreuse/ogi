function ControllerPrpTabAdministratif($scope, Page, $routeParams, ServiceConfiguration, ServiceObject, ServiceAlert, $http, $log, Utils) {

    // Init
    $scope.httpGetCurrentType.success(function() {
        // Indique quel panel doit s'afficher. Pour qu'un panel s'affiche, il faut que la propriété du bien soit définie.
        $scope.panel = {
            sale: !Utils.isUndefinedOrNull($scope.prp.sale),
            rent: !Utils.isUndefinedOrNull($scope.prp.rent)
        }
    });

    $scope.computeCommisionPercent = function() {
        $scope.prp.sale.commissionPercent = ($scope.prp.sale.commission / $scope.prp.sale.price * 100).round(2);
    }

    $scope.computeCommision = function() {
        $scope.prp.sale.commission = ($scope.prp.sale.commissionPercent/100 * $scope.prp.sale.price).round(2);
        $scope.computePriceFinal();
    }

    /** Compute price for client according to price without commission and commission */
    $scope.computePriceFinal = function() {
        //number + NaN => NaN
        $scope.prp.sale.priceFinal = Number.parseInt($scope.prp.sale.price) + Number.parseInt($scope.prp.sale.commission || 0);
        $scope.computeCommisionPercent();
    }

    $scope.computeRentFinalPrice = function() {
        var finalPrice = 0;
        if (!$scope.prp.rent.serviceChargeIncluded && $scope.prp.rent.serviceCharge) {
            finalPrice += Number.parseFloat($scope.prp.rent.serviceCharge);
        }
        if (!Utils.isUndefinedOrNull($scope.prp.rent.price)) {
            finalPrice += Number.parseFloat($scope.prp.rent.price);
        }

        $scope.prp.rent.priceFinal = finalPrice;
    }

    //Lors de l'ouverture du panel, il faut créer et attacher un objet vente/location au bien pour qu'il puisse être renseigné
    $scope.panelShowHide = function(type) {
        // toggle value
        $scope.panel[type] = !$scope.panel[type]

        // If show panel => attach object to realproperty
        if($scope.panel[type]) {
            // Create attribute into property
            $scope.prp[type] = Object.create(type == 'sale' ? sale : rent);
        }
        else { // Hide panel => remove object to realproperty
            $scope.prp[type] = null;
        }
    }


    // Calendar
    $scope.open = {
        'mandateStartDate': false,
        'mandateEndDate': false,
        'estimationDate': false,
        'freeDate' : false
    }

    $scope.openCalendar = function($event, calendarName) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.open[calendarName] = true;
    };

    $scope.dateOptions = {
        'year-format': "'yyyy'",
        'starting-day': 1
    };

};

