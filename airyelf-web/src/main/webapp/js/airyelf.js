/*global angular*/
(function () {
    'use strict';

    var module = angular.module('airyelf', ['ngResource']);

    module.controller('Main', ['$scope', '$resource', function (scope, resource) {
        scope.portletDefinitions = resource('action/portlet-definition').query();
        scope.urlOf = function (def) {
            return def.url + '/' + def.groupId.replace(/\./g, '/') + '/' + def.version + '/' + def.artifactId + '.html';
        };
    }]);

}());
