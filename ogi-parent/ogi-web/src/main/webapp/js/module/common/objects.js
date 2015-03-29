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
    CLIENT : {"type": "CLIENT", "label": "<b>Les points forts</b>\n-> \n-> \n-> \n \n<i>Informations complémentaires</i>\n-> \n->"},
    VITRINE : {"type": "VITRINE", "label": "<b>Les points forts</b>\n-> \n-> \n\n-> m² habitables\n-> \n-> \n-> \n-> "},
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
    var extractDocumentsByZone = function (documents, zoneList) {
        return (documents || []).filter(elt => elt.type.zoneList == zoneList).sort((a, b) => a.order - b.order);
    }
    var extractDocumentsByType = function (documents, typeCode) {
        return (documents || []).filter(elt => elt.type.code == typeCode).sort((a, b) => a.order - b.order);
    }

    this.rooms = [];

    // Init object from parameter
    for(var key in prpFromAPI){
        // Replace null for undefined. Need to select with angular JS. If value is null angular create a new option instead of select option with value ""
        this[key] = prpFromAPI[key] == null ?  undefined : prpFromAPI[key];
    }

    this.dpe = {};
    angular.extend(this.dpe, dpe);
    angular.extend(this.dpe, prpFromAPI.dpe);

    // Object.create renvoie un nouvel object avec les propriétés de l'objet source en tant que prototype.
    // Cependant JSON.stringify ne tient pas compte du prototype. La solution que j'ai trouvé est de copier toutes
    // les propriétés de l'object dans sa destination via un extends
    this.descriptions = {};
    angular.extend(this.descriptions, angular.copy(description));
    if(!utilsObject.isUndefinedOrNull(prpFromAPI.descriptions)) {
        angular.extend(this.descriptions, prpFromAPI.descriptions);
    }

    // if address is defined copy attribute into object previously created
    this.address = {};
    angular.extend(this.address, angular.copy(address)); // Populate address field from predefined object
    if(!utilsObject.isUndefinedOrNull(prpFromAPI.address)) {
        angular.extend(this.address, prpFromAPI.address); // override JS values with server values
    }

    // Override photo only if not exist or empty
    if(utilsObject.isEmpty(prpFromAPI.documents)) {
        this.photos = [];
    }
    else {
        this.photos = extractDocumentsByType(prpFromAPI.documents, "PHOTO");
    }

    if(!utilsObject.isUndefinedOrNull(prpFromAPI.sale)) {
        this.sale = {};
        this.sale = angular.extend(this.sale, sale);
        angular.extend(this.sale, prpFromAPI.sale);
        this.sale.documents = extractDocumentsByZone(prpFromAPI.documents, "SALE");
    }

    if(!utilsObject.isUndefinedOrNull(prpFromAPI.rent)) {
        this.rent = {};
        this.rent = angular.extend(this.rent, rent);
        angular.extend(this.rent, prpFromAPI.rent);
        this.rent.documents = extractDocumentsByZone(prpFromAPI.documents, "RENT");
    }

    this.init();


    this.toJSON = function() {
        var prpJSON = {};
        angular.extend(prpJSON, this);

        prpJSON.documents=this.photos//
        .concat(this.sale ? this.sale.documents : [])//
        .concat(this.rent ? this.rent.documents : []);
        delete prpJSON.photos;


        return prpJSON;
    }
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
    sold : false,
    documents : []
}

var rent = {
}

var ogiDocument = {
    techid: null,
    order: -1,
    name: null,
    path: null,
    type: null
}