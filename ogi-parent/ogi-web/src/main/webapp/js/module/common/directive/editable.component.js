angular.module('myApp.common').component('ogiEditable', {
    controller: OGIEditableController,
    bindings: {
        hero: '='
    }
});

function OGIEditableController() {
  var _this = this;
  console.log('OGIEditableController');

  _this.toogleEditable = function () {
    console.log('coool');
  };
}


angular.module('myApp.common').directive('ogiEditableDirective', function () {
  return {
    restrict: 'E',
    scope: {
      editFunction: '='
    },
    link: function (scope, element, attrs) {
      scope.toogleEditable = function () {
        console.log('directive coool');
      };
    }
  };
});