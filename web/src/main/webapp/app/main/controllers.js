/**
 * Main app controllers.
 * Author: Bill Lv<billcc.lv@hotmail.com>
 * Date: 2014-12-17
 */
mainApp.controller('mainController', function ($scope, $upload, $timeout, $http, mainService) {
    $scope.model = {
        uid: null,
        checkins: [],
        photos: [],
        uploads: [],
        newPhotos: [],
        newCheckin: {
            record: null
        },
        uploaded: false,
        checkedin: false,
        timeId: null
    };

    $scope.init = function (uid) {
        $scope.model.uid = uid;
        $scope.getCheckins(uid);
    };

    $scope.getCheckins = function (uid) {
        mainService.getCheckins(uid).then(function (response) {
            $scope.model.checkins = response;
        });
    };

    $scope.checkin = function () {
        var invalid = $scope.checkinForm.$invalid;
        if (!invalid) {
            $scope.model.checkedin = true;
            var checkin = {};
            checkin.uid = $scope.model.uid;
            checkin.record = $scope.model.newCheckin.record;
            checkin.photoIds = $scope.model.uploads;
            mainService.checkin(checkin).then(function (response) {
                if (response) {
                    $scope.model.checkins.push(response);
                    $('.release-success').show();
                    $('#attachmentName').prop('disabled', true);
                    clearTimeout($scope.timeId);
                    $scope.timeId = setTimeout(function () {
                        $('.release-success').hide();
                        $('#attachmentName').prop('disabled', false);
                        $scope.model.checkedin = false;
                        $scope.model.uploaded = false;
                        $('.act-btn').click();
                    }, 2e3);
                }
            });
            $scope.model.newCheckin.record = '';
            $scope.model.uploads = [];
            $scope.model.newPhotos = [];
        }
    };

    $scope.usingFlash = false;

    $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);

    /* Utitlty function to convert a canvas to BLOB */
    $scope.dataURLToBlob = function (dataURL) {
        var BASE64_MARKER = ';base64,';
        if (dataURL.indexOf(BASE64_MARKER) == -1) {
            var parts = dataURL.split(',');
            var contentType = parts[0].split(':')[1];
            var raw = parts[1];
            return new Blob([raw], {type: contentType});
        }
        var parts = dataURL.split(BASE64_MARKER);
        var contentType = parts[0].split(':')[1];
        var raw = window.atob(parts[1]);
        var rawLength = raw.length;

        var uInt8Array = new Uint8Array(rawLength);

        for (var i = 0; i < rawLength; ++i) {
            uInt8Array[i] = raw.charCodeAt(i);
        }
        return new Blob([uInt8Array], {type: contentType});
    }

    $scope.$watch('model.newPhotos', function () {
        for (var i = 0; i < $scope.model.newPhotos.length; i++) {
            var file = $scope.model.newPhotos[i];

            if (file != null) {
                if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {

                    // Ensure it's an image
                    if (file.type.match(/image.*/)) {
                        var reader = new FileReader();

                        reader.onload = function (readerEvent) {
                            var image = new Image();

                            image.onload = function () {
                                // Resize the image
                                var canvas = document.createElement('canvas');

                                // iphone4 width 320
                                var MAX_WIDTH = 260;

                                if (image.width > MAX_WIDTH) {
                                    image.height *= MAX_WIDTH / image.width;
                                    image.width = MAX_WIDTH;
                                }

                                canvas.width = image.width;
                                canvas.height = image.height;

                                canvas.getContext('2d').drawImage(image, 0, 0, image.width, image.height);
                                var dataUrl = canvas.toDataURL('image/jpeg');

                                // file.dataUrl = readerEvent.target.result;
                                file.dataUrl = dataUrl;

                                var resizedImage = $scope.dataURLToBlob(dataUrl);
                                var formData = new FormData($("form[id*='checkinForm']")[0]);
                                formData.append('file', resizedImage, file.name);

                                $http.post('/checkin/upload', formData, {
                                    transformRequest: angular.identity,
                                    headers: {'Content-Type': undefined}
                                }).success(function (response) {
                                    var photoIds = response.photoIds;
                                    for (var pos = 0; pos < photoIds.length; pos++) {
                                        $scope.model.uploads.push(photoIds[pos]);
                                    }
                                    $scope.model.uploaded = true;
                                    $('#attachmentName').prop('disabled', true);
                                }).error(function () {
                                    // do nothing
                                });
                            };

                            image.src = readerEvent.target.result;
                        }

                        reader.readAsDataURL(file);

                    }
                }
            }
        }
    });
});
