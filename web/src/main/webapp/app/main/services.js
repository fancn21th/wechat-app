/**
 * Main app services.
 * Author: Bill Lv<billcc.lv@hotmail.com>
 * Date: 2014-12-20
 */
mainApp.service('mainService', function ($http, $q, $timeout) {
    this.getCheckins = function (uid) {
        var d = $q.defer();
        $http.get('/uid/' + uid + '/checkins').success(function (response) {
            d.resolve(response);
        });
        return d.promise;
    };

    this.checkin = function (checkin) {
        var d = $q.defer();
        $http.post('/checkins/', checkin).success(function (response) {
            d.resolve(response);
        });
        return d.promise;
    };
});
