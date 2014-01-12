function ControllerPrpTabAdministratif($scope, Page, $routeParams, ServiceConfiguration, ServiceObject, ServiceAlert, $http, $log ,$modal) {

    $scope.computeCommisionPercent = function() {
        $scope.prp.sale.commissionPercent = ($scope.prp.sale.commission / $scope.prp.sale.price * 100).round(2);
    }

    $scope.computeCommision = function() {
        $scope.prp.sale.commission = ($scope.prp.sale.commissionPercent/100 * $scope.prp.sale.price).round(2);
        $scope.computePriceFinal();
    }

    /**
     * Compute price for client according to price without commission and commission
     */
    $scope.computePriceFinal = function() {
        //number + NaN => NaN
        $scope.prp.sale.priceFinal = Number.parseInt($scope.prp.sale.price) + Number.parseInt($scope.prp.sale.commission || 0);
        $scope.computeCommisionPercent();
    }




    // Calendar
    $scope.open = {
        'mandateStartDate': false,
        'mandateEndDate': false,
        'estimationDate': false
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

