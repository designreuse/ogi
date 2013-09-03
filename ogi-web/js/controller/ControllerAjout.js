function ControllerAjout($scope, Page, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log) {
    Page.setTitle("Ajouter un bien : "+$routeParams.type);
    var tempReference = Math.random().toString(36).substring(7);
    $log.log("tempReference="+tempReference);

    $scope.prp = {};
    $scope.prp.address = {};

    $scope.markers = [];
    $scope.mapOptions = {
        center: ServiceConfiguration.MAP_CENTER,
        zoom: 12,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };


    $scope.clickMap = function($event, $params) {
        addOrMoveMarker($params[0].latLng);
    };

    $scope.resetMap = function() {
        angular.forEach($scope.markers, function (item) {
            item.setMap(null);
        });
        $scope.markers = [];
        $scope.map.setCenter(ServiceConfiguration.MAP_CENTER);
        $scope.map.setZoom(ServiceConfiguration.MAP_ZOOM);
    };

    $scope.findPositionByAddress = function () {
        var address = $scope.prp.address.street;
        new google.maps.Geocoder().geocode( { 'address': address}, function(results, status) {
            console.log(results);
            $scope.setPlaces(results);

            if (status == google.maps.GeocoderStatus.OK) {
                if(results.length == 1) {
                    $scope.usePlace(0);
                }
                else if(results.length >= 2)  {
                    $scope.openGeoloc();
                }

            } else {
                ServiceAlert.addError("Erreur de  geolocalisation : " + status);
            }

            // google response is asynchronous so angularjs don't know scope has changed
            $scope.$apply();
        });
    }


    // If no marker add one and move it if exist
    function addOrMoveMarker(latLng) {
        // Only add 1 marker which can drag
        if($scope.markers.length == 0) {
            $scope.markers.push(new google.maps.Marker({
                map: $scope.map,
                position: latLng,
                draggable : true
            }));
        }
        else {
            $scope.markers[0].setPosition(latLng);
        }
    };

    $scope.usePlace = function (index) {
        $scope.map.setCenter($scope.places[index].geometry.location);
        addOrMoveMarker($scope.places[index].geometry.location);
        $scope.closeGeoloc();
    }

    // http://www.ramandv.com/blog/using-google-maps-with-angularjs/

    // ##### UPLOAD #####
    $scope.options = {
        url: ServiceConfiguration.API_URL+"/rest/photo/",
        type : "POST", // The HTTP request method for the file uploads
        dataType : "json", //The type of data that is expected back from the server.
        limitMultiFileUploads : 2, //To limit the number of files uploaded with one XHR request, set the following option to an integer greater than 0
        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
        messages: {
            maxNumberOfFiles: 'Nombre maximum de fichiers dépassé',
            acceptFileTypes: 'Type du fichier incompatible avec des images',
            maxFileSize: 'Fichier trop gros',
            minFileSize: 'Fichier trop petit'
        },
        formData : [{ name: 'reference', value: tempReference}]
    };

    // ##### SORTABLE #####
    $scope.sortableOptions = {
        update: function(e, ui) {
            //var order = $('#list-photos').sortable('serialize');
            console.log("update");
        },
        placeholder: 'highlight' // class of fantom item
    };


    // ##### MODAL #####
    $scope.places = [];
    $scope.shouldBeOpenGeoloc = false;
    // It's impossible to reaffect or modify variable in sons controller
    $scope.setPlaces = function (p) {
        $scope.places = p;
    }

    var opts = {
        backdropFade: true,
        dialogFade: true
    };

    $scope.optsGeoloc = opts;
    $scope.openGeoloc = function () {
        $scope.shouldBeOpenGeoloc = true;
    };
    $scope.closeGeoloc = function () {
        $scope.shouldBeOpenGeoloc = false;
    };

};

function FileDestroyController($scope, $http) {
        var file = $scope.file,
            state;
        if (file.url) {
            file.$state = function () {
                return state;
            };
            file.$destroy = function () {
                state = 'pending';
                return $http({
                    url: file.deleteUrl,
                    method: file.deleteType
                }).then(
                    function () {
                        state = 'resolved';
                        $scope.clear(file);
                    },
                    function () {
                        state = 'rejected';
                    }
                );
            };
        } else if (!file.$cancel && !file._index) {
            file.$cancel = function () {
                $scope.clear(file);
            };
        }
    }



