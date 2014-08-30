// 'ngSanitize' => for ng-bind-html
angular.module('myApp.dashboard', ['ngRoute', 'ui.bootstrap', 'ngSanitize']);


// ###### ROUTES ######
angular.module('myApp.dashboard').config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/dashboard', {templateUrl: 'js/module/dashboard/view/batch.html', controller: "ControllerBatch"})
        .when('/accueil', {templateUrl: 'js/module/dashboard/view/home.html', controller: "ControllerHome"});
}]);
