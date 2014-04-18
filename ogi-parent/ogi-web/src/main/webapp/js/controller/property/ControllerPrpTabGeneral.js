function ControllerPrpTabGeneral($scope, Page, $routeParams, ServiceConfiguration, ServiceAlert, $http, $log) {

    // ##### MAP #####
    $scope.markers = [];
    $scope.mapOptions = {
        center: ServiceConfiguration.MAP_CENTER,
        zoom: 12,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };


    $scope.clickMap = function($event, $params) {
        addOrMoveMarker($params[0].latLng, false);
        updateGPS($params[0].latLng.lat(), $params[0].latLng.lng());
    };

    $scope.resetMap = function() {
        angular.forEach($scope.markers, function (item) {
            item.setMap(null);
        });
        $scope.markers = [];
        $scope.map.setCenter(ServiceConfiguration.MAP_CENTER);
        $scope.map.setZoom(ServiceConfiguration.MAP_ZOOM);

        updateGPS(null, null);
    };

    $scope.findPositionByAddress = function () {
        // If no element of address was enter => exit
        if($scope.saveData.address.isEmpty()) { return;};

        var address = $scope.saveData.address.street + " " + ($scope.saveData.address.postalCode || "") + " " + ($scope.saveData.address.city || "");
        $log.log("Lookup for "+address);

        new google.maps.Geocoder().geocode( { 'address': address}, function(results, status) {
            $scope.places = results;

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
    function addOrMoveMarker(latLng, center) {
        // Only add 1 marker which can drag
        if($scope.markers.length == 0) {
            var m = new google.maps.Marker({
                map: $scope.map,
                position: latLng,
                draggable : true
            });
            m.addListener("dragend", function() {
                updateGPS(this.getPosition().lat(), this.getPosition().lng());
            });
            $scope.markers.push(m);
        }
        else {
            $scope.markers[0].setPosition(latLng);
        }
        if(center) {
            $scope.map.setCenter(latLng);
        }
    };

    function updateGPS(lat, lng) {
        $scope.saveData.address.latitude = lat;
        $scope.saveData.address.longitude = lng;
    }

    $scope.usePlace = function (index) {
        var location = $scope.places[index].geometry.location;

        updateGPS(location.lat(), location.lng());
        addOrMoveMarker(location, true);

        $scope.closeGeoloc();
    }

    // http://www.ramandv.com/blog/using-google-maps-with-angularjs/

    // ##### UPLOAD #####
    // http://blueimp.github.io/jQuery-File-Upload/angularjs.html
    $scope.options = {
        url: ServiceConfiguration.API_URL+"/rest/document/",
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
        formData : [{ name: 'reference', value:  $scope.tempReference}, { name: 'type', value:  "PHOTO"}]
    };

    // Listen to fileuploaddone event
    $scope.$on('fileuploaddone', function(e, data){

        // Extract file from response (file contains document)
        var file = data._response.result.files[0];
        // Set order to table length
        file.document.order = $scope.prp.photos.length+1;

        // Add photo to property
        $scope.prp.photos.push(file.document);

        // Search index of just upload file to remove it to queue
        var index = -1;
        for (var i = 0; i < $scope.queue.length;  i++) {
            if($scope.queue[i].name == file.name) {
                index = i;
                break;
            }
        }
        $scope.queue.splice(index, 1);
    });

    // ##### SORTABLE #####
    $scope.sortableOptions = {
        // This event is triggered when sorting has stopped.
        stop: function(e, ui) {
            $scope.prp.photos.forEach(function(element, index, array) {
                element.order = index+1;
            });
        },
        placeholder: 'highlight' // class of fantom item
    };

    $scope.deletePhoto = function(index) {
        $scope.prp.photos.splice(index, 1);
    }


    // ##### MODAL #####
    $scope.places = [];
    $scope.shouldBeOpenGeoloc = false;

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


    // Init according to object
    var mapFirstLoad = true;
    $scope.onMapIdle = function(param) {
        if(mapFirstLoad) {
            mapFirstLoad = false;
            // If latitude and longitude => add marker on Map
            if($scope.prp.address != null && !$scope.prp.address.isLatLngEmpty()) {
                addOrMoveMarker(new google.maps.LatLng($scope.prp.address.latitude, $scope.prp.address.longitude), true);
            }
        }
    }
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
