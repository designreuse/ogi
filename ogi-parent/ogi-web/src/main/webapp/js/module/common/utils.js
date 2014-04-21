function fctName(fct) {
    return /\W*function\s+([\w\$]+)\(/.exec(fct.toString())[1]
}


// Extends javascript number
Number.prototype.round = function(places) {
    return +(Math.round(this + "e+" + places)  + "e-" + places);
}
