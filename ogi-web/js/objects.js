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

var description = {
    ETAT : {"type": "ETAT", "label": ""},
    APP : {"type": "APP", "label": ""},
    VITRINE : {"type": "VITRINE", "label": ""},
    WEBSITE_PERSO : {"type": "WEBSITE_PERSO", "label": ""},
    WEBSITE_AUTRE : {"type": "WEBSITE_AUTRE", "label": ""}
};



function PropertyJS(prpFromAPI) {
    // Init object from parameter
    for(var key in prpFromAPI){
        this[key] = prpFromAPI[key];
    }

    this.description = Object.create(description);
    if(!utilsObject.isUndefinedOrNull(prpFromAPI.descriptions)) {
        angular.extend(this.description, prpFromAPI.description);
    }

    this.address = Object.create(address);
    // if address is defined copy attribute into object previously created
    if(!utilsObject.isUndefinedOrNull(prpFromAPI.address)) {
        angular.extend(this.address, prpFromAPI.address);
    }

    // Override photo only if not exist or empty
    if(utilsObject.isEmpty(prpFromAPI.photos)) {
        this.photos = [];
    }

    this.init();
}

PropertyJS.prototype = {
    init : function () {
    }
}


var labelOther = {
    "techid": -1,
    "type": "NOTYPE",
    "label": "Autre"
}