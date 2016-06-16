angular.module('myApp.property').controller("ControllerPrpTabVisitSummary",
function ($scope, Page, ServiceVisitSummary) {
  var _this = this;

  _this.summaries = [];
  _this.summary;


  $scope.httpGetCurrentType.then(function() {
    _this.summary = emptySummary();

    // Get all summaries for current real property
    ServiceVisitSummary.getSummaries($scope.prp.reference).then((result) => _this.summaries = result.data);
  });

  _this.openCalendar = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    _this.calendarOpen = true;
  };
  _this.dateOptions = {
    'formatYear': "yyyy",
    'startingDay': 1
  };
  _this.dateFormat= "dd/MM/yyyy";


  /** Add visit to database and append it to list */
  _this.addVisitSummary = function () {
    ServiceVisitSummary.addSummary(_this.summary).then((result) => {
      console.log(result);
      _this.summaries.unshift(result.data);
      _this.summary = emptySummary();
    });
  };




  /** Create empty summary with defaut date */
   function emptySummary () {
    return {
      date : new Date(),
      client: '',
      description : '',
      property: {
        reference: $scope.prp.reference
      }
    };
  };
});

