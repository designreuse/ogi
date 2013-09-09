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

var prp = {
    address : Object.create(address),
    photos : []
};