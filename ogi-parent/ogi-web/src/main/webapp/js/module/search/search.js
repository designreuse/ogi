// 'ngSanitize' => for ng-bind-html
angular.module('myApp.search',
    ['ngRoute', 'ui.bootstrap', 'ngSanitize']);


// ###### ROUTES ######
angular.module('myApp.search').config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/search', {templateUrl: 'js/module/search/view/searchResult.html', controller: "ControllerSearch"});
}]);
