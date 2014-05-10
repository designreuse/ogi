function ControllerPrpTabRoom($scope, Page, $modal, ServiceConfiguration, ServiceLabel, ServiceObject) {
    // All floors
    $scope.floors = [];
    ServiceLabel.getLabels("floor").success(function(data) {
        // Need to preserve pointer reference of $scope.floor because it may pass to ModalRoomInstanceCtrl before
        // ajax call is resolved
        $scope.floors.push.apply($scope.floors, data);
        $scope.floors.push(labelOther);
    });

    // Image to display when room doesn't have photo
    $scope.imgDefault = ServiceConfiguration.API_URL+"/photos/error.jpg?size=300,300";

    // ###### CRUD FUNCTIONS ######
    $scope.add = function() {
       var modalInstance = $modal.open({
           templateUrl: 'modalRoom.html',
           controller: ModalRoomInstanceCtrl,
           resolve: {
               title: function(){ return "Ajouter une pièce"; },
               actionLabel: function(){ return "Ajouter"; },
               room : function () { return {}; },
               floors : function() { return $scope.floors; },
               photos : function() { return $scope.prp.photos; }
           }
       }).result.then(function(roomToAdd) {
               // Add room to prp.
               $scope.prp.rooms.push(roomToAdd);
           }, function () {});
    }

    $scope.update = function(room) {
        // Compute index of selected room because it may doesn't have a techid to found it after
        var index = $scope.prp.rooms.indexOf(room);

        // Deep copy room in order to not modify original when user clic on cancel button
        var roomToModify = angular.copy(room);
        var modalInstance = $modal.open({
            templateUrl: 'modalRoom.html',
            controller: ModalRoomInstanceCtrl,
            resolve: {
                title: function(){ return "Modifier la pièce";},
                actionLabel: function(){ return "Modifier"; },
                room : function () { return roomToModify;},
                floors : function() { return $scope.floors; },
                photos : function() { return $scope.prp.photos; }
            }
        }).result.then(function(roomModal) {
                // Delete it
                $scope.prp.rooms.splice(index, 1);
                // Add it
                $scope.prp.rooms.splice(index, 0, roomModal);
            }, function () {});
    };

    $scope.delete = function(room) {
        var index = $scope.prp.rooms.indexOf(room);
        if (index > -1) {
            $scope.prp.rooms.splice(index, 1);
        }
    };
};

var ModalRoomInstanceCtrl = function ($scope, $modalInstance, $q,
                                      ServiceConfiguration, ServiceObject, ServiceLabel,
                                      $http, actionLabel, room, title, floors, photos) {
    $scope.title = title;
    $scope.room = room;
    $scope.floors = floors;
    $scope.action = actionLabel;
    $scope.photos = photos;

    // Convert null to undefined in order to select works
    $scope.room.orientation = $scope.room.orientation || undefined;

    // Extract label of current room to preserve pointer
    var floorLabel = ServiceObject.getObject($scope.floors, {"label":room.floor}, ["label"]);

    // Map to indicate which image is selected
    $scope.photoActive = {};
    $scope.photos.forEach(function(elt, idx) {
        var selected =false;
        if(room.photo && elt.techid==room.photo.techid) {
            selected = true;
        }
        $scope.photoActive[elt.techid] = selected;
    });

    $scope.saveData = {
        floor : floorLabel
    };
    $scope.newLabel = {
        "floor" : {"display": false, "value" : "", "type" : "FLOOR"}
    };

    // when user click on a photo
    $scope.selectPhoto = function(photo) {
        // Deselect all photos
        Object.keys($scope.photoActive).map(function(value, index) {
            $scope.photoActive[value] = false;
        });
        // Selected selected photo
        $scope.photoActive[photo.techid] = true;

        // Add photo to room
        room.photo = photo;
    };

    /** Display input when floor value change to "other" */
    $scope.floorChange = function () {
        if($scope.saveData.floor.label == "Autre") {
            $scope.displayNewLabel("floor", true);
        }
    }

    /**
     * Change display state
     * @param label type of label to change. Must be a key of $scope.displayNewLabel
     * @param display true or false
     */
    $scope.displayNewLabel = function (label, displayed) {
        $scope.newLabel[label].display = displayed;

        // When hide input if data input is empty => select none
        if(!displayed && !$scope.newLabel[label].value) {
            $scope.saveData.floor = undefined;
        }
    }

    $scope.ok = function () {
        // By default save is resolved.
        var deferred  = $q.defer();
        deferred.resolve();
        var pSave = deferred.promise;

        // Input text isn't displayed => save into prp room only label
        if(!$scope.newLabel.floor.display && $scope.saveData.floor) {
            $scope.room.floor = $scope.saveData.floor.label;
        }
        // Input text is displayed => consideration of the value
        if($scope.newLabel.floor.display && $scope.newLabel.floor.value) {
            // Save label
            pSave = ServiceLabel.saveLabel($scope.newLabel.floor.type, $scope.newLabel.floor.value)//
            .success(function(data) {
                $scope.floors.push(data);
            });
            // Write in room value of new label
            $scope.room.floor = $scope.newLabel.floor.value;
        }


        // Close modal only when promise is success
        pSave.then(function() {
            // Spread room to caller
            $modalInstance.close($scope.room);
        });

    };
};

