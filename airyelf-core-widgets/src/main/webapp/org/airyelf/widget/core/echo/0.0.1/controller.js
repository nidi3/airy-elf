angular.module('org.airyelf.widget.core-0.0.1').controller('EchoController', function ($scope, $http, info) {
    $scope.text = '';
    $scope.send = function () {
        $http.get(info.serverUrl + '/action/echo?text=' + $scope.text)
            .success(function (data) {
                $scope.echo = data;
            });
    };
});