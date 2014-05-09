function ControllerPrpTabRoom($scope, Page, $modal, ServiceConfiguration, ServiceLabel) {
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

    $scope.add = function() {
       var modalInstance = $modal.open({
           templateUrl: 'modalRoom.html',
           controller: ModalRoomInstanceCtrl,
           resolve: {
               title: function(){ return "Ajouter une pièce"; },
               actionLabel: function(){ return "Ajouter"; },
               room : function () { return {}; },
               floors : function() { return $scope.floors; }
           }
       }).result.then(onSuccessModalRoom, function () {});
    }

    function onSuccessModalRoom(room) {
        // Add room to prp.
        $scope.prp.rooms.push(room);
    }

    $scope.update = function(room) {
        var modalInstance = $modal.open({
            templateUrl: 'modalRoom.html',
            controller: ModalRoomInstanceCtrl,
            resolve: {
                title: function(){ return "Modifier la pièce";},
                actionLabel: function(){ return "Modifier"; },
                room : function () { return room;},
                floors : function() { return $scope.floors; }
            }
        }).result.then(onSuccessModalRoom, function () {});
    };

    $scope.delete = function(room) {
        var index = $scope.prp.rooms.indexOf(room);
        if (index > -1) {
            $scope.prp.rooms.splice(index, 1);
        }
    };
};

var ModalRoomInstanceCtrl = function ($scope, $modalInstance, ServiceConfiguration, ServiceObject, $http, actionLabel, room, title, floors) {
    $scope.title = title;
    $scope.room = room;
    $scope.floors = floors;
    $scope.action = actionLabel;

    // Extract label of current room to preserve pointer
    var floorLabel = ServiceObject.getObject($scope.floors, {"label":room.floor}, ["label"]);

    $scope.saveData = {
        floor : floorLabel
    };

    $scope.ok = function () {
        // Save into prp room only label
        if( $scope.room.floor) {
            $scope.room.floor = $scope.room.floor.label;
        }

        // Spread room to caller
        $modalInstance.close($scope.room);
    };
};

