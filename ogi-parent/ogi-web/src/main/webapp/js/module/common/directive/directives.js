angular.module('myApp.config').directive('minusPlus', function() {
    return {
        restrict: 'A',
        link: function (scope, elem, attrs) {
            //console.log(attrs.minusPlus);

            // Get input and span in dom
            var input = elem.children("input");
            var spans = elem.children("span");

            // First span => minus
            spans.eq(0).click(function() {
                // If not value define it to zero
                var inputVal = input.val() == "" ? 1 : input.val();

                var value = parseInt(inputVal);
                if(!isNaN(value) && value > 0) {
                    input.val(value-1);
                    //ngModel listens for "input" event, so to "fix"
                    input.trigger('input');
                }
            });

            // Second span => plus
            spans.eq(1).click(function() {
                // If not value define it to zero
                var inputVal = input.val() == "" ? 0 : input.val();

                var value = parseInt(inputVal);
                if(!isNaN(value)) {
                    input.val(value+1);
                    //ngModel listens for "input" event, so to "fix"
                    input.trigger('input');
                }
            });

        }
    }
});


/**
 * Directive positionnant la classe "active" sur le menu courant.
 *
 * L'attribut ogi-navigation doit être positionné sur l'élément englobant du menu
 * L'attribut ogi-navigation-url doit être positionné sur les liens constituant le menu. La valeur du lien sera
 * utilisé pour déterminé le menu courant. Si le lien contient le path courant de l'url => class active
 */
angular.module('myApp.config').directive('ogiNavigation', function() {
    return {
        restrict: 'A',
        link: function (scope, elem, attrs) {
            var eltsNavigation = elem.find("a[ogi-navigation-url]");

            // Listen route change
            scope.$on('$routeChangeSuccess', function(angularEvent, current, previous) {
                eltsNavigation.each(function(index) {
                    var $elt = $(this);
                    if($elt.attr("href").includes(current.originalPath)) {
                        $elt.addClass("active");
                    }
                    else {
                        $elt.removeClass("active");
                    }
                });
            });

        }
    }
});


angular.module('myApp.config').directive('sortBy', function () {
    return {
        templateUrl: 'sort-by.html',
        restrict: 'E',
        transclude: true,
        replace: true,
        scope: {
            sortdir: '=',
            sortedby: '=',
            sortvalue: '@',
            onsort: '='
        },
        link: function (scope, element, attrs) {
            scope.sort = function () {
                if (scope.sortedby == scope.sortvalue)
                    scope.sortdir = scope.sortdir == 'asc' ? 'desc' : 'asc';
                else {
                    scope.sortedby = scope.sortvalue;
                    scope.sortdir = 'asc';
                }
                scope.onsort(scope.sortedby, scope.sortdir);
            }
        }
    };
});

angular.module("sort-by.html", []).run(["$templateCache", function($templateCache) {
    $templateCache.put("sort-by.html",  "" +
        "<a ng-click=\"sort(sortvalue)\" class=\"pointer\">"+
        "   <span ng-show=\"sortedby == sortvalue\">"+
        "       <i ng-class=\"{true: 'glyphicon glyphicon-arrow-down', false: 'glyphicon glyphicon-arrow-up'}[sortdir == 'asc']\"></i>"+
        "   </span>"+
        "   <span ng-transclude=\"\"></span>"+
        "</a>" +
        "");
}]);

/*
myApp.directive('minusPlus', function() {
    return {
        restrict: 'A',
        scope : {
            ngModel: '='
        },
        link: function (scope, elem, attrs) {
            //console.log(attrs.plus);
            console.log(scope);
            scope.$watch('plus', function (newVal) {
                console.log('plus', newVal);
            });

            // Get input and span in dom
            var input = elem.children("input");
            var spans = elem.children("span");

            // First span => minus
            spans.eq(0).click(function() {
                    // If not value define it to zero
                    var inputVal = input.val() == "" ? 1 : input.val();

                    var value = parseInt(inputVal);
                    if(!isNaN(value) && value > 0) {
                        input.val(value-1);
                    }
                }
            );

            // Second span => plus
            spans.eq(1).click(function() {
                    // If not value define it to zero
                    var inputVal = input.val() == "" ? 0 : input.val();

                    var value = parseInt(inputVal);
                    if(!isNaN(value)) {
                        input.val(value+1);
                        scope.ngModel=value+1;
                        scope.$apply();
                    }
                }
            );

            // When input value change
            input.change(function() {
                scope.ngModel = $(this).val();
                scope.$apply();
            });
        }
    }
});
*/