// 'ngSanitize' => for ng-bind-html
var myApp = angular.module('myApp',
    ['ngRoute', 'myApp.property', 'myApp.owner', 'myApp.dashboard',
    'myApp.common', 'myApp.passerelle', 'myApp.config',
    'myApp.search']);



// ###### ROUTES ######
myApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.otherwise({redirectTo: '/accueil'});
}]);
