/*global angular*/
(function () {
    'use strict';

    var module = angular.module('airyelf', ['ngResource']);

    module.controller('Main', ['$scope', '$resource', function (scope, resource) {
        scope.page = resource('action/page/:page').get({page: 'dummy'});
        scope.urlOf = function (def) {
            return def.url + '/' + def.groupId.replace(/\./g, '/') + '/' + def.artifactId + '/' + def.version + 'content.html';
        };
    }]);

    module.directive('widgetContainer', function () {
        return {
            restrict: 'EA',
            replace: true,
            template: '<ul class="widgetContainer">' +
                '  <li id="{{widget.id}}" ng-repeat="widget in containerWidgets()">' +
                '    <div class="widget">' +
                '      <h3>{{widget.definition.name}}</h3>' +
                '      <div widget="widget"></div>' +
                '    </div>' +
                '  </li>' +
                '</ul>',
            scope: {},
            controller: function ($scope, $attrs) {
                $scope.widgets = function () {
                    return $scope.$parent.page.widgets;
                };
                $scope.containerWidgets = function (value) {
                    var w, ws = $scope.widgets();
                    if (ws) {
                        if (value) {
                            ws[$attrs.id] = value;
                        } else {
                            w = ws[$attrs.id];
                            if (!w) {
                                w = ws[$attrs.id] = [];
                            }
                        }
                    }
                    return w;
                };
            },
            link: function (scope, element) {
                var findIndex = function (container, id) {
                    var i;
                    for (i = 0; i < container.length; i += 1) {
                        if (container[i].id == id) {
                            return i;
                        }
                    }
                };
                element.sortable({
                    connectWith: '.widgetContainer',
                    placeholder: 'placeholder',
                    helper: function (event, elem) {
                        return '<h3>' + elem.find('h3').text() + '</h3>';
                    },
                    stop: function () {
                        var i, ids = element.sortable('toArray'), ws = scope.containerWidgets(), res = [];
                        for (i = 0; i < ids.length; i += 1) {
                            res.push(ws[findIndex(ws, ids[i])]);
                        }
                        scope.containerWidgets(res);
                        scope.$apply();
                    },
                    receive: function (event, ui) {
                        var source = scope.widgets()[ui.sender.attr('id')],
                            i = findIndex(source, ui.item.attr('id'));

                        scope.widgets()[this.id].push(source[i]);
                        source.splice(i, 1);
                    }
                });
                element.disableSelection();
            }

        };
    });
    module.directive('widget', function () {
        var waitingForInit = [],
            modules = {};
        return {
            link: function (scope, element, attrs) {
                var def = scope.$eval(attrs.widget).definition,
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
