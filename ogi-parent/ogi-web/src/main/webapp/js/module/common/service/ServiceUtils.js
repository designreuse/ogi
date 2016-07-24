myApp.factory('Utils', function () {
  return utilsObject;
});


var utilsObject = {
  isUndefinedOrNull: function (obj) {
    return obj == undefined || obj === null;
  },
  isEmpty: function (obj) {
    return this.isUndefinedOrNull(obj) || obj.length == 0;
  }
};
