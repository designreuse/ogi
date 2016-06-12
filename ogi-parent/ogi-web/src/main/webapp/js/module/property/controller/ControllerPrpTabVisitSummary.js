angular.module('myApp.property').controller("ControllerPrpTabVisitSummary",
function ($scope, Page, ServiceVisitSummary) {
  var _this = this;

  _this.summaries = [];


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

  _this.summary = emptySummary();


  /** Add visit to database and append it to list */
  _this.addVisitSummary = function () {
    ServiceVisitSummary.addSummary(_this.summary).then((result) => {
      console.log(result);
      _this.summaries.unshift(result.data);
      _this.summary = emptySummary();
    });
  };


  ServiceVisitSummary.getSummaries($scope.prp.reference).then((result) => _this.summaries = result);


  /** Create empty summary with defaut date */
   function emptySummary () {
    return {
      date : new Date(),
      client: '',
      txt : ''
    };
  };
});

