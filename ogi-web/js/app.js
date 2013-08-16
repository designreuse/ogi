var myApp = angular.module('myApp', ['ngRoute']);
myApp.config(['$httpProvider', function($httpProvider) {
    // Just setting useXDomain to true is not enough. AJAX request are also send with the X-Requested-With header, which
    // indicate them as being AJAX. Removing the header is necessary, so the server is not rejecting the incoming request.
    $httpProvider.defaults.useXDomain = true;

    // X-Requested-With 	mainly used to identify Ajax requests. Most JavaScript frameworks send this header with value of XMLHttpRequest
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}
]);

myApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/biens', {templateUrl: 'js/views/prpList.html', controller: Ctrl}).
        when('/biens/:prpId', {templateUrl: 'js/views/prpDetail.html', controller: DetailCtrl}).
        otherwise({redirectTo: '/biens'});
}]);

myApp.factory('Page', function(){
    var pageTitle = "Untitled";
    return {
        title:function(){
            return pageTitle;
        },
        setTitle:function(newTitle){
            pageTitle = newTitle;
        }
    }
});

myApp.factory('ServiceObjectChecked', function(){
    var pageTitle = "Untitled";
    return {
        addChecked:function(objects, value){
            angular.forEach(objects, function (o, key) {
                o.checked = value;
            });
            return objects;
        },
        getChecked:function(objects){
            return objects.filter(function (o) {
                return o.checked;
            });
        }
    }
});