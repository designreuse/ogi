angular.module('myApp.property').controller("ControllerPrpTabRoom",
function ($scope, Page, $uibModal, ServiceConfiguration, ServiceLabel, ServiceObject) {
    $scope.labels = {};
    getLabels("room", "roomTypes");
    getLabels("floor", "floors");
    getLabels("wall", "walls");

    function getLabels(type, vScope) {
        $scope[vScope] = [];

        ServiceLabel.getLabels(type).success(function (data) {
            $scope.labels[vScope] = data;
            $scope.labels[vScope].push(labelOther);
        });
    }

    // Image to display when room doesn't have photo
    $scope.imgDefault = ServiceConfiguration.API_URL+"/photos/error.jpg?size=300,300";

    // ###### CRUD FUNCTIONS ######
    $scope.add = function() {
       var modalInstance = $uibModal.open({
           templateUrl: 'modalRoom.html',
           controller: "ModalRoomInstanceCtrl",
           resolve: {
               title: function(){ return "Ajouter une pièce"; },
               actionLabel: function(){ return "Ajouter"; },
               room : function () { return {}; },
               labels : function() { return $scope.labels; },
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
        var modalInstance = $uibModal.open({
            templateUrl: 'modalRoom.html',
            controller: "ModalRoomInstanceCtrl",
            resolve: {
                title: function(){ return "Modifier la pièce";},
                actionLabel: function(){ return "Modifier"; },
                room : function () { return roomToModify;},
                labels : function() { return $scope.labels; },
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
});

angular.module('myApp.property').controller("ModalRoomInstanceCtrl",
function ($scope, $modalInstance, $q,
                                      ServiceConfiguration, ServiceObject, ServiceLabel,
                                      $http, actionLabel, room, title, labels, photos) {
    $scope.title = title;
    $scope.room = room;
    // All labels. Key are : floors, roomTypes, walls
    $scope.labels = labels;
    $scope.action = actionLabel;
    $scope.photos = photos;

    // Convert null to undefined in order to select works
    $scope.room.orientation = $scope.room.orientation || undefined;

    // Map to indicate which image is selected
    $scope.photoActive = {};
    $scope.photos.forEach(function(elt, idx) {
        var selected =false;
        if(room.photo && elt.techid==room.photo.techid) {
            selected = true;
        }
        $scope.photoActive[elt.techid] = selected;
    });


    // Attr must be identical between saveData and newLabel and room
    $scope.saveData = {
        // Extract label of current room to preserve pointer
        floor :     ServiceObject.getObject($scope.labels.floors,       {"label": room.floor},      ["label"]),
        roomType :  ServiceObject.getObject($scope.labels.roomTypes,    {"label": room.roomType},   ["label"]),
        wall :      ServiceObject.getObject($scope.labels.walls,        {"label": room.wall},       ["label"])
    };
    $scope.newLabel = {
        "floor" :       {"display": false, "value" : "", "type" : "FLOOR",  "listValues" : "floors"},
        "roomType" :    {"display": false, "value" : "", "type" : "ROOM",   "listValues" : "roomTypes"},
        "wall" :        {"display": false, "value" : "", "type" : "WALL",   "listValues" : "walls"}
    };

    // when user click on a photo
    $scope.selectPhoto = function(photo) {
        // Deselect all photos
        Object.keys($scope.photoActive).map(function(value, index) {
            // Don't current photo in order to toggle is value
            if(value != photo.techid) {
               $scope.photoActive[value] = false;
            }
        });

        // Toggle selected photo
        $scope.photoActive[photo.techid] = !$scope.photoActive[photo.techid];

        // Add photo to room only if photo is selected
        if($scope.photoActive[photo.techid]) {
            room.photo = photo;
        }
        else {
            room.photo = null;
        }

    };

    /** Display input when floor value change to "other" */
    $scope.labelChange = function (attrName, fnToExecute) {
        if($scope.saveData[attrName] && $scope.saveData[attrName].label == "Autre") {
            $scope.displayNewLabel(attrName, true);
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
            $scope.saveData[label]= undefined;
        }
    }

    $scope.ok = function () {
        var promiseFloor = saveLabel("floor");
        var promiseRoomType = saveLabel("roomType");
        var promiseWall = saveLabel("wall");


        // Close modal only when promise is success
        $q.all([promiseFloor, promiseRoomType, promiseWall]).then(function() {
            // Spread room to caller
            $modalInstance.close($scope.room);
        });
    };

    /**
     * If new label => save label in database
     * Save into $scope.room label according to attrName
     * @param attrName name of attr in saveData and newLabel and room
     * @returns {Function|promise}
     */
    function saveLabel(attrName) {
        // Input text isn't displayed => save into prp room only label
        if(!$scope.newLabel[attrName].display && $scope.saveData[attrName]) {
            $scope.room[attrName] = $scope.saveData[attrName].label;
        }

        // By default save is resolved.
        var deferred  = $q.defer();
        deferred.resolve();
        var pSave = deferred.promise;

        // Input text is displayed => consideration of the value
        if($scope.newLabel[attrName].display && $scope.newLabel[attrName].value) {
            // Save label
            pSave = ServiceLabel.saveLabel($scope.newLabel[attrName].type, $scope.newLabel[attrName].value)//
                .success(function(data) {
                    // Add to array the new value
                    $scope.labels[$scope.newLabel[attrName].listValues].push(data);
                });
            // Write in room value the new label
            $scope.room[attrName] = $scope.newLabel[attrName].value;
        }
        return pSave;
    }
});

