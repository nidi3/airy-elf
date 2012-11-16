/*global angular*/
angular.module('org.airyelf.widget.core-0.0.1').controller('TimeController', function ($scope, $timeout) {
    'use strict';

    var setTime = function () {
        $scope.time = Date.now();
        $timeout(setTime, 900);
    };
    $timeout(setTime, 900);
});