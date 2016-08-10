angular.module('myApp.property').controller("ControllerPrpRead",
  function ($scope, $rootScope, Page, $routeParams, ServiceConfiguration, ServiceDPE, $http, $log, ServiceUrl, Lightbox) {
    Page.setTitle("Fiche client du bien : " + $routeParams.prpRef);

    // Initialise photospehere options
    Lightbox.photoSphereOptions = {
      loading_img: 'http://photo-sphere-viewer.js.org/assets/photosphere-logo.gif',
      navbar: 'autorotate zoom caption fullscreen',
      default_fov: 80,
      mousewheel: false,
      fisheye: true,
      lang: {
        autorotate: 'Rotation automatique',
        zoom: 'Zoom',
        zoomOut: 'Zoomer',
        zoomIn: 'Dezoomer',
        download: 'Télécharger',
        fullscreen: 'Plein écran',
        markers: 'Markers',
        gyroscope: 'Gyroscope'
      }
    };

    $scope.carouselInterval = 5000;
    $scope.slides = [];

    $scope.prp = null;
    $http.get(ServiceConfiguration.API_URL + "/rest/property/" + $routeParams.prpRef)
      .then(function (response, status, headers) {
        $scope.prp = new PropertyJS(response.data);
        $scope.prp.photos.forEach(function (img, index) {
         addSlide(img.url + '?size=600,400', img.name, index);
        });

        $scope.prp.photosSphere.forEach(function (img, index) {
          addSlide(
            img.url + '?size=600,400&crop=600,400&&sizeMode=inversed_automatic&w=photosphere',
            img.name,
            $scope.prp.photos.length + index
          );
        });
      });



    $scope.displayLiveable = function () {
      return ['HSE', 'APT'].indexOf($scope.prp.category.code) > -1;
    };

    $scope.displayBusiness = function () {
      return ['BSN'].indexOf($scope.prp.category.code) > -1;
    };

    $scope.descriptionClient = function () {
      if ($scope.prp) {
        return $scope.prp.descriptions.CLIENT.label.replace(/\n/g, '<br />');
      } else { return ""; }
    };

    $scope.kwhImage = function () {
      return ServiceDPE.getKWHImageUrl(_.get($scope.prp, "dpe.kwh"), 200, ServiceConfiguration.API_URL);
    };
    $scope.gesImage = function () {
      return ServiceDPE.getGESImageUrl(_.get($scope.prp, "dpe.ges"), 200, ServiceConfiguration.API_URL);
    };


    var addSlide = function (url, text, index) {
      $scope.slides.push({
        image: url,
        text: text,
        id: index,
      });
    };

    $scope.openLightboxModal = function (index) {
      const ogiTypeToLightbox = {'PHOTO': 'image', 'PHOTO_SPHERE' : 'photosphere'};

      Lightbox.openModal(_.map($scope.prp.photos.concat($scope.prp.photosSphere), function (elt) {
        return {
          url: elt.url,
          type: ogiTypeToLightbox[elt.type.code]
        };
      }), index);
    };

    $scope.openRoomDetails = function (index) {
      Lightbox.openModal($scope.prp.rooms, index, {templateUrl: "js/module/property/view/prpClientView-room-modal.html"});
    };

  });



