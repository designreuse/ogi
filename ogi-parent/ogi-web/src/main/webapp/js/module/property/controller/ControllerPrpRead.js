angular.module('myApp.property').controller("ControllerPrpRead",
function ($scope, $rootScope, Page, $routeParams, ServiceConfiguration, ServiceDPE, $http, $log, ServiceUrl, Lightbox) {
  Page.setTitle("Fiche client du bien : "+$routeParams.prpRef);

  $scope.carouselInterval = 5000;
  $scope.slides = [];

  $scope.prp = null;;
  $http.get(ServiceConfiguration.API_URL+"/rest/property/"+$routeParams.prpRef)
  .success(function (data, status, headers) {
      $scope.prp = new PropertyJS(data);
      console.log('---', $scope.prp);


      $scope.prp.photos.forEach(function(img) {
        addSlide(img.url + '?size=600,400', img.name);
      });
  });


  $scope.descriptionClient = function() {
    if($scope.prp) {
      return $scope.prp.descriptions.CLIENT.label.replace(/\n/g, '<br />');
    }
    else {
      return "";
    }
  };

  $scope.kwhImage = function() {
    return ServiceDPE.getKWHImageUrl(_.get($scope.prp, "dpe.kwh"), 200, ServiceConfiguration.API_URL);
  };
  $scope.gesImage = function() {
    return ServiceDPE.getGESImageUrl(_.get($scope.prp, "dpe.ges"), 200, ServiceConfiguration.API_URL);
  };


  var addSlide = function(url, text) {
    $scope.slides.push({
      image: url,
      text: text
    });
  };

  $scope.openLightboxModal = function (index) {
    Lightbox.openModal(_.map($scope.prp.photos, function(elt) {
      return {
        'url': elt.url
      };
    }), index);
  };

  $scope.openRoomDetails = function (index) {
    Lightbox.openModal($scope.prp.rooms, index, {templateUrl: "js/module/property/view/prpClientView-room-modal.html"});
  };

});



