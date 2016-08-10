angular.module('ngPhotosphere', [])
  .directive('photosphere', function($timeout) {
    return {
      restrict: 'A',
      template: '',
      transclude: true,
      link: function link(scope, element, attrs, controller, transcludeFn) {
        // Add attr options to user specific options
        var options = angular.extend({}, scope.psOptions, {
          panorama: attrs.psSrc,
          container: element[0],
          caption: attrs.psCaption
        });
        $timeout(function() {
          var p = new PhotoSphereViewer(options);
        }, 300);
      },
      scope: {
        psOptions: '=psOptions'
      }
    };
  });