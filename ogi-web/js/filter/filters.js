/**
 * Img filter.
 * Available size : thumb, medium, big
 */
myApp.filter('img', function() {
    return function(url, size) {
        return url+'?size='+size;
    };
});


myApp.filter('desc', function() {
    return function(descriptions, type) {
        for(var i = 0; i < descriptions.length; i++) {
            if(descriptions[i].type == type) {
                return descriptions[i].label;
            }
        }
        return "";
    };
});


// Display essential of an address
myApp.filter('address', function() {
    return function(address) {
        // If array empty return empty string
        if(utilsObject.isUndefinedOrNull(address) || address.length == 0) {  return ""; }

        // else return first address
        var s = address[0].street || "";
        s += " ";
        s += address[0].postalCode || "";
        s += " ";
        s += address[0].city || "";

        return s;
    };
});

myApp.filter('prpLink', function(Utils, ServiceUrl) {
    return function(properties) {
        // If array empty return empty string
        if(Utils.isUndefinedOrNull(properties) || properties.length == 0) {  return ""; }

        // else return first address
        var s = "";
        properties.forEach(function(elt, index) {
            s += "<a href=\""+ServiceUrl.urlProperty(elt.reference)+"\">"+elt.reference+"</a>";
        });

        return s;
    };
});

/** Filter to display area. Add m² to end */
myApp.filter('area', function(Utils) {
    return function(area) {
         if(!Utils.isUndefinedOrNull(area)) {
             return area + " m²";
         }
        return "";
    };
});
