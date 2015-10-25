angular.module('myApp.common', [])
  .run(['$rootScope', 'ServiceUrl', function($rootScope, ServiceUrl) {
    $rootScope.ServiceUrl = ServiceUrl;
  }]);