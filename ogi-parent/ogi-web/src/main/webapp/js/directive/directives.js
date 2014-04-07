myApp.directive('minusPlus', function() {
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
            }
            );

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
            }
            );

        }
    }
});


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