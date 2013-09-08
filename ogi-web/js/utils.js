function fctName(fct) {
    return /\W*function\s+([\w\$]+)\(/.exec(fct.toString())[1]
}