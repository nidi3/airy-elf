/*global angular*/
(function () {
    'use strict';

    var module = angular.module('airyelf', ['ngResource']);

    module.controller('Main', ['$scope', '$resource', function (scope, resource) {
        scope.portletDefinitions = resource('action/portlet-definition').query();
        scope.urlOf = function (def) {
            return def.url + '/' + def.groupId.replace(/\./g, '/') + '/' + def.artifactId + '/' + def.version + 'content.html';
        };
        scope.widgets = {
            column1: scope.portletDefinitions
        };
    }]);

    module.directive('widget', function () {
        var waitingForInit = [],
            modules = {};
        return {
            link: function (scope, element, attrs) {
                var def = scope.$eval(attrs.widget),
                    moduleUrl = def.url + '/' + def.groupId.replace(/\./g, '/') + '/',
                    artifactUrl = moduleUrl + def.artifactId + '/' + def.version + '/',
                    moduleName = def.groupId + '-' + def.version;
                $.get(artifactUrl + 'content.html', {},
                    function (data) {
                        var match,
                            regex = /<script src="(.+?)"><\/script>/g,
                            pos = 0,
                            result = '',
                            loading = 0,
                            finishIfAllScriptsLoaded = function () {
                                if (loading === 0) {
                                    var node = $(result).appendTo(element),
                                        injector = angular.bootstrap(node, [moduleName]),
                                        rootScope = injector.get('$rootScope');

                                    modules[moduleName] = rootScope;
                                    rootScope.$emit('airyelf.init', {
                                        modules: modules,
                                        serverUrl: def.url
                                    });
                                }
                            },
                            loadScript = function (url) {
                                loading += 1;
                                $.getScript(url, function () {
                                    loading -= 1;
                                    finishIfAllScriptsLoaded();
                                });
                            },
                            includeContent = function () {
                                while (match = regex.exec(data)) {
                                    result += data.substring(pos, match.index);
                                    pos = match.index + match[0].length;
                                    loadScript(artifactUrl + match[1]);
                                }
                                result += data.substring(pos);
                                finishIfAllScriptsLoaded();
                            };

                        switch (modules[moduleName]) {
                        case 'initing':
                            waitingForInit.push(includeContent);
                            break;
                        case 'inited':
                            includeContent();
                            break;
                        default:
                            modules[moduleName] = 'initing';
                            waitingForInit.push(includeContent);
                            $.getScript(moduleUrl + '$init/' + def.version + "/init.js", function () {
                                angular.forEach(waitingForInit, function (fn) {
                                    fn();
                                });
                                modules[moduleName] = 'inited';
                            });
                        }
                    }, 'html');
            }
        };
    });

}());
