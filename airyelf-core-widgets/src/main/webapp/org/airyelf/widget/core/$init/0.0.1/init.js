/*global angular*/
(function () {
    'use strict';

    angular.module('org.airyelf.widget.core-0.0.1', ['ngResource'])
        .factory('info', function ($rootScope) {
            var res = {};
            $rootScope.$on('airyelf.init', function (event, data) {
                angular.extend(res, data);
            });
            return res;
        });
}());