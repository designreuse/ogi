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