/**
 * Img filter.
 * Available size : thumb, medium, big
 */
myApp.filter('img', function () {
  return function (url, size) {
    return url + '?size=' + size;
  };
});


myApp.filter('desc', function () {
  return function (descriptions, type) {
    for (var i = 0; i < descriptions.length; i++) {
      if (descriptions[i].type == type) {
        return descriptions[i].label;
      }
    }
    return "";
  };
});


// Display essential of an address
myApp.filter('address', function () {
  return function (address) {
    // If array empty return empty string
    if (utilsObject.isUndefinedOrNull(address) || address.length == 0) {
      return "";
    }

    // else return first address
    var s = address[0].street || "";
    s += " ";
    s += address[0].postalCode || "";
    s += " ";
    s += address[0].city || "";

    return s;
  };
});

/** Filter to display area. Add m² to end */
myApp.filter('area', function (Utils) {
  return function (area) {
    if (!Utils.isUndefinedOrNull(area)) {
      return area + " m²";
    }
    return "";
  };
});


myApp.filter('boolean', function () {
  return function (b) {
    if (b === true) {
      return "Oui";
    }
    else if (b === false) {
      return "Non";
    }
    else {
      return undefined;
    }
  };
});

myApp.filter('nl2br', function () {
  return function (s) {
    return (s || '').replace(new RegExp('\n', 'ig'), '<br />');
  };
});


// ###### LINKS ######
/**
 * Create a link to property edit page.
 * @param reference : property reference
 */
myApp.filter('prpLink', function (Utils, ServiceUrl) {
  return function (reference) {
    return technicalPrpLink([{"reference": reference}], Utils, ServiceUrl);
  };
});

/**
 * Create a link to property edit page.
 * @param property : list of property object
 */
myApp.filter('prpLinks', function (Utils, ServiceUrl) {
  return function (property) {
    return technicalPrpLink(property, Utils, ServiceUrl);
  };
});

function technicalPrpLink(properties, Utils, ServiceUrl) {
  // If array empty return empty string
  if (Utils.isUndefinedOrNull(properties) || properties.length == 0) {
    return "";
  }

  // else return links seperated by a <br />
  var s = "";
  properties.forEach(function (elt, index) {
    s += "<a href=\"" + ServiceUrl.urlPropertyEdit(elt.reference) + "\">" + elt.reference + "</a><br />";
  });

  return s;
};


// OWNER
/**
 * Create a link to owner edit page.
 * @param owners : list of owners object
 */
myApp.filter('ownerLinks', function (Utils, ServiceUrl) {
  return function (owners) {
    return technicalOwnerLink(owners, Utils, ServiceUrl);
  };
});

function technicalOwnerLink(owners, Utils, ServiceUrl) {
  // If array empty return empty string
  if (Utils.isUndefinedOrNull(owners) || owners.length == 0) {
    return "";
  }

  // else return links seperated by a <br />
  var s = "";
  owners.forEach(function (elt, index) {
    s += "<a href=\"#" + ServiceUrl.urlOwner(elt.techid) + "\">" + elt.name + "</a><br />";
  });

  return s;
};
