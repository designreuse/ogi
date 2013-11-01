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

    if(!utilsObject.isUndefinedOrNull(prpFromAPI.descriptions)) {
        if(utilsObject.isUndefinedOrNull(prpFromAPI.descriptions.ETAT)) {
            this.descriptions.ETAT = {"type": "ETAT", "label": ""};
        }
        if(utilsObject.isUndefinedOrNull(prpFromAPI.descriptions.APP)) {
            this.descriptions.APP = {"type": "APP", "label": ""};
        }
        if(utilsObject.isUndefinedOrNull(prpFromAPI.descriptions.VITRINE)) {
            this.descriptions.VITRINE = {"type": "VITRINE", "label": ""};
        }
        if(utilsObject.isUndefinedOrNull(prpFromAPI.descriptions.WEBSITE_PERSO)) {
            this.descriptions.WEBSITE_PERSO = {"type": "WEBSITE_PERSO", "label": ""};
        }
        if(utilsObject.isUndefinedOrNull(prpFromAPI.descriptions.WEBSITE_AUTRE)) {
            this.descriptions.WEBSITE_AUTRE = {"type": "WEBSITE_AUTRE", "label": ""};
        }
    }

    this.address = Object.create(address);
    // if address is defined copy attribute into object previously created
    if(!utilsObject.isUndefinedOrNull(prpFromAPI.address)) {
        angular.extend(this.address, prpFromAPI.address);
    }

    // Override address only if not exist or empty
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