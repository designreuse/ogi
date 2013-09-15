var address = {
    "street": null,
    "additional": null,
    "postalCode": null,
    "city": null,
    "latitude": null,
    "longitude": null,
    isEmpty : function() {
        return this.street == null && this.additional == null && this.postalCode == null && this.city == null && this.latitude == null && this.longitude == null;
    },
    isLatLngEmpty : function() {
        return this.latitude == null && this.longitude == null;
    }
};



function PropertyJS(prpFromAPI) {
    // Init object from parameter
    for(var key in prpFromAPI){
        this[key] = prpFromAPI[key];
    }
    this.descriptionsSimple = [];
    this.init();
}

PropertyJS.prototype = {
    address : Object.create(address),
    descriptions: [],
    photos : [],
    init : function () {
        // Create array of descriptions
        angular.forEach(this.descriptions, function(value, key) {
            this.descriptionsSimple[key] = value;
        });
    }
}