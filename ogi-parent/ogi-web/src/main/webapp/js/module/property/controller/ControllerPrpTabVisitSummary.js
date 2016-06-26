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
  _this.openCalendarUpdate = function(summary, $event) {
    $event.preventDefault();
    $event.stopPropagation();
    summary.calendarOpen = true;
  };

  _this.dateOptions = {
    'formatYear': "yyyy",
    'startingDay': 1
  };
  _this.dateFormat= "dd/MM/yyyy";


  /** Add visit to database and append it to list */
  _this.addVisitSummary = function () {
    ServiceVisitSummary.addSummary(_this.summary).then((result) => {
      _this.summaries.unshift(result.data);
      _this.summary = emptySummary();
    });
  };
  _this.deleteSummary = function (summaryToDelete) {
    summaryToDelete.deleted = true;
    ServiceVisitSummary.deleteSummary(summaryToDelete.techid)
      .catch(function () {
        summaryToDelete.deleted = false;
      });
  };


  _this.updateSummary = function (summaryToUpdate, callbackToExecute) {
    ServiceVisitSummary.updateSummary(summaryToUpdate)
      .then(function () {
        // Call the callback (toogle edit mode)
        callbackToExecute();
        summaryToUpdate.updated = true;
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

angular.module('myApp.property').controller("ControllerVisitEditable",
function ($scope, Page, ServiceVisitSummary) {
  var _this = this;

  var editable = false;
  _this.toogleEditable = function () {
    editable = !editable;
  };

  _this.isEditable = function () {
    return editable;
  };

});

