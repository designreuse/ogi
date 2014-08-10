angular.module('myApp.common').controller("MainCtrl",
function ($scope, Page, Utils) {
    $scope.page = Page;
    $scope.utils = Utils;
});