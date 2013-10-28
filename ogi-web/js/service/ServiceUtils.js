myApp.factory('Utils', function() {
    var service = {
        isUndefinedOrNull: function(obj) {
            return obj == undefined || obj===null;
        },
        isEmpty: function(obj) {
            return this.isUndefinedOrNull(obj) || obj.length == 0;
        }
    }

    return service;
});