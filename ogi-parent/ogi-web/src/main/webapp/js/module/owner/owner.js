
// 'ngSanitize' => for ng-bind-html
var modOwner = angular.module('myApp.owner', ['ngRoute', 'ui.bootstrap', 'ngSanitize', 'dialogs']);


// ###### ROUTES ######
modOwner.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/proprietaires', {templateUrl: 'js/module/owner/view/ownerList.html', controller: ControllerOwnerList}).
        when('/proprietaires/modifier/:techid', {templateUrl: 'js/module/owner/view/ownerFormAdd.html', controller: ControllerOwnerModify}).
        when('/proprietaires/ajouter/', {templateUrl: 'js/module/owner/view/ownerFormAdd.html', controller: ControllerOwnerAdd});
}]);
