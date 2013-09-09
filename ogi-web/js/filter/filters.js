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

