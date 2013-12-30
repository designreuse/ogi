myApp.factory('Page', function() {
    var pageTitle = "OGI";
    return {
        title:function() {
            return pageTitle;
        },
        setTitle:function(newTitle) {
            pageTitle = newTitle;
        }
    }
});

myApp.factory('ServiceObjectChecked', function(){
    return {
        addChecked:function(objects, value){
            angular.forEach(objects, function (o, key) {
                o.checked = value;
            });
            return objects;
        },
        getChecked:function(objects){
            return objects.filter(function (o) {
                return o.checked;
            });
        }
    }
});


myApp.factory('ServiceAlert', function(){
    var alerts = [ ];
    return {
        addSuccess:function(msg){
            this.addAlert({ type: 'success', msg: msg});
        },
        addError:function(msg){
            this.addAlert({ type: 'error', msg: msg});
        },
        addAlert:function(obj){
            obj.date = new Date();
            alerts.push(obj);
        },
        closeAlert:function(index) {
            alerts.splice(index, 1);
        },
        getAlerts:function(){
            return alerts;
        }
    }
});

myApp.factory('ServiceObject', function(Utils) {
    return {
        /**
         * Return array's element corresponding to label to find. It's for keep same pointer
         * @param array array into find element
         * @param objectToFind object to find in array. Comparison will be compute according to attr into array attrToCompare
         * @param attrToCompare list of attributes on which comparison will be compute
         * @returns {*}
         */
        getObject:function(array, objectToFind, attrToCompare){
            if(Utils.isUndefinedOrNull(array) || Utils.isUndefinedOrNull(objectToFind)) {
                return null;
            }
            var arrayEqual =  array.filter(function (o) {
                var equals = true;
                attrToCompare.forEach(function(element, index, array) {
                    if(o[element] != objectToFind[element]) {
                        equals = false;
                    }
                });
                return equals;
            });
            return Utils.isEmpty(arrayEqual) ? null : arrayEqual[0];
        }
    }
});


myApp.factory('ServiceUrl', function() {
    var alerts = [ ];
    return {
        urlProperty : function(reference){
            return "#/biens/modifier/"+reference;
        },
        urlOwner : function(techid){
            return "#/proprietaires/modifier/"+techid;
        },
    }
});
