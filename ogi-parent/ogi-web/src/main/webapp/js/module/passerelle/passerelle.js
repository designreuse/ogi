// 'ngSanitize' => for ng-bind-html
angular.module('myApp.passerelle', ['ngRoute', 'ui.bootstrap', 'ngSanitize']);


// ###### ROUTES ######
angular.module('myApp.passerelle').config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/passerelle/recapitulatif', {templateUrl: 'js/module/passerelle/view/passRecap.html', controller: "ControllerPasserelleRecap"});
}]);
