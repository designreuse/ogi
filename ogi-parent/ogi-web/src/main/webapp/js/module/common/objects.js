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
    APP : {"type": "APP", "label": ""},
    CLIENT : {"type": "CLIENT", "label": ""},
    VITRINE : {"type": "VITRINE", "label": ""},
    WEBSITE_PERSO : {"type": "WEBSITE_PERSO", "label": ""},
    WEBSITE_AUTRE : {"type": "WEBSITE_AUTRE", "label": ""}
};

var dpe = {
    "kwh" : null,
    "classificationKWh" : null,
    "ges" : null,
    "classificationGes" : null
};



function PropertyJS(prpFromAPI) {
    // Init object from parameter
    for(var key in prpFromAPI){
        // Replace null for undefined. Need to select with angular JS. If value is null angular create a new option instead of select option with value ""
        this[key] = prpFromAPI[key] || undefined;
    }

    this.dpe = {};
    angular.extend(this.dpe, dpe);
    angular.extend(this.dpe, prpFromAPI.dpe);

    // Object.create renvoie un nouvel object avec les propriétés de l'objet source en tant que prototype.
    // Cependant JSON.stringify ne tient pas compte du prototype. La solution que j'ai trouvé est de copier toutes
    // les propriétés de l'object dans sa destination via un extends
    this.descriptions = {};
    angular.extend(this.descriptions, description);
    if(!utilsObject.isUndefinedOrNull(prpFromAPI.descriptions)) {
        angular.extend(this.descriptions, prpFromAPI.descriptions);
    }

    // if address is defined copy attribute into object previously created
    this.address = {};
    angular.extend(this.address, address); // Populate address field from predefined object
    if(!utilsObject.isUndefinedOrNull(prpFromAPI.address)) {
        angular.extend(this.address, prpFromAPI.address); // override JS values with server values
    }

    // Override photo only if not exist or empty
    if(utilsObject.isEmpty(prpFromAPI.photos)) {
        this.photos = [];
    }
    this.photos.sort(function(a, b) {
        return a.order - b.order;
    });

    if(!utilsObject.isUndefinedOrNull(prpFromAPI.sale)) {
        this.sale = {};
        this.sale = angular.extend(this.sale, sale);
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