function ControllerPrpTabRoom($scope, Page, $modal, ServiceConfiguration) {
    // Image to display when room doesn't have photo
    $scope.imgDefault = ServiceConfiguration.API_URL+"/photos/error.jpg?size=300,300";

    $scope.add = function() {
       var modalInstance = $modal.open({
           templateUrl: 'modalAddRoom.html',
           controller: ModalRoomInstanceCtrl,
           resolve: {
               title: function(){
                   return "Ajouter";
               },
               room : function () {
                   return {};
               }
           }
       }).result.then(function (room) {
           $scope.prp.rooms.push(room);
       }, function () {
       });;
    }

    $scope.add();
};

var ModalRoomInstanceCtrl = function ($scope, $modalInstance, ServiceConfiguration, $http, room, title) {
    $scope.newType = {"label" : null}; // Have to use object else in function ok value is not up to date
    $scope.title = title;
    $scope.room = room;

    $scope.ok = function () {
        $modalInstance.close($scope.room);
    };
};

