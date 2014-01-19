var address = {
    "street": null,
    "additional": null,
    "postalCode": null,
    "city": null,
    "country": null,
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
    "VITRINE" : {"type": "VITRINE", "label": ""},
    WEBSITE_PERSO : {"type": "WEBSITE_PERSO", "label": ""},
    WEBSITE_AUTRE : {"type": "WEBSITE_AUTRE", "label": ""}
};



function PropertyJS(prpFromAPI) {
    // Init object from parameter
    for(var key in prpFromAPI){
        this[key] = prpFromAPI[key];
    }

    this.descriptions = Object.create(description);
    if(!utilsObject.isUndefinedOrNull(prpFromAPI.descriptions)) {
        angular.extend(this.descriptions, prpFromAPI.descriptions);
    }

    // if address is defined copy attribute into object previously created
    this.address = null;
    if(!utilsObject.isUndefinedOrNull(prpFromAPI.address)) {
        this.address = Object.create(address);
        angular.extend(this.address, prpFromAPI.address);
    }

    // Override photo only if not exist or empty
    if(utilsObject.isEmpty(prpFromAPI.photos)) {
        this.photos = [];
    }
    this.photos.sort(function(a, b) {
        return a.order - b.order;
    });

    if(!utilsObject.isUndefinedOrNull(prpFromAPI.sale)) {
        this.sale = Object.create(sale);
        angular.extend(this.sale, prpFromAPI.sale);
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

var typeOther = {
    "label": "Autre",
    "category": {
        "code": "XXX",
        "label": "XXX"
    }
}

var sale = {
}

var rent = {
}