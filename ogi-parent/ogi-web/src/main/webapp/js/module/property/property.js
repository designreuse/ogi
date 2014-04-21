// 'ngSanitize' => for ng-bind-html
angular.module('myApp.property',
    ['ngRoute', 'ui.bootstrap', 'ui.map',
        'ui.sortable', 'blueimp.fileupload', 'ui.slider', 'ngSanitize',
        'dialogs'
    ]);


// ###### ROUTES ######
angular.module('myApp.property').config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/biens', {templateUrl: 'js/module/property/view/prpList.html', controller: ControllerList}).
        when('/biens/modifier/:prpRef', {templateUrl: 'js/module/property/view/prpFormGlobal.html', controller: ControllerPrpModify}).
        when('/biens/ajouter/:type', {templateUrl: 'js/module/property/view/prpFormGlobal.html', controller: ControllerPrpAdd});
}]);
