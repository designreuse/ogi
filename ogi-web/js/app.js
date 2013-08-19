/** Module for configuration. Provide ServiceConfiguration */
var moduleConf = angular.module('ModuleConfiguration', [])//
    .factory('ServiceConfiguration', function() {
    return {
        API_URL: "http://localhost:8080/ogi-ws"
    }
});

var myApp = angular.module('myApp', ['ngRoute', 'ui.bootstrap', 'ModuleConfiguration']);

// Config $http for CORS
myApp.config(['$httpProvider', function($httpProvider) {
    // Just setting useXDomain to true is not enough. AJAX request are also send with the X-Requested-With header, which
    // indicate them as being AJAX. Removing the header is necessary, so the server is not rejecting the incoming request.
    $httpProvider.defaults.useXDomain = true;

    // X-Requested-With 	mainly used to identify Ajax requests. Most JavaScript frameworks send this header with value of XMLHttpRequest
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);

// Config routes
myApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/biens', {templateUrl: 'js/views/prpList.html', controller: ControllerList}).
        when('/biens/modifier/:prpRef', {templateUrl: 'js/views/prpDetail.html', controller: DetailCtrl}).
        when('/biens/ajouter/:type', {templateUrl: 'js/views/prpFormulaireAjout.html', controller: ControllerAjout}).
        otherwise({redirectTo: '/biens'});
}]);