'use strict';
import angular from 'angular';

/**
 * @ngdoc function
 * @name qrGuiApp.controller:MainCtrl
 * @description # MainCtrl Controller of the qrGuiApp
 */
angular
    .module('qrGuiApp')
    .controller(
        'RoutineCtrl', ['HtmlUtilityFactory', '$scope', '$rootScope', 'constant',
            'ListUtilityFactory', '$uibModal', 'EmiaRestUtilityFactory',
            'UtilErrorsFactory', '$q',
            function (HtmlUtilityFactory, $scope, $rootScope, constant,
                      ListUtilityFactory, $uibModal, EmiaRestUtilityFactory,
                      UtilErrorsFactory, $q) {

                $rootScope.activeMenu = 'routines';

                const routineController = this;

                routineController.deleteRoutineTitle = "Cancella Routine";
                routineController.deleteQueryRoutineTitle = "Cancella Query da Routine";
                routineController.executeRoutineTitle = "Esegui Routine";
                routineController.newRoutineTitle = "Crea nuova Routine";

                const templateSetFunction = 'SetRoutineListTemplate';

                /**
                 * Lista routine
                 */
                const getRoutines = function () {

                    $q
                        .all(
                            [
                                EmiaRestUtilityFactory
                                    .GetRoutines(),
                                EmiaRestUtilityFactory
                                    .GetQueriesByTipCateg()])
                        .then(
                            function (response) {
                                let temi18Rou;
                                let routine;
                                if (UtilErrorsFactory
                                    .CheckResponse(
                                        routineController,
                                        response).hasErrors)
                                    return;

                                $rootScope.routines = [];

                                /**
                                 * per ogni routine creo N record
                                 * per le query con il json
                                 * scompattato come se fosse
                                 * l'oggetto query
                                 */
                                const routines = response[0].entity;

                                const queries = response[1].entity;

                                /**
                                 * issue 6684, filtro per le
                                 * categorie di cui ho visiblità ,
                                 * per le altre tolgo le query
                                 */
                                for (routine in routines) {

                                    for (const query in queries) {

                                        for (temi18Rou in routines[routine].temi18RouQues) {

                                            for (const temi18RouQue in queries[query].temi18RouQues) {

                                                if (routines[routine].temi18RouQues[temi18Rou].id.que == queries[query].temi18RouQues[temi18RouQue].id.que
                                                    && routines[routine].temi18RouQues[temi18Rou].id.insQue == queries[query].temi18RouQues[temi18RouQue].id.insQue) {

                                                    routines[routine].temi18RouQues[temi18Rou].temi15UteQue = queries[query];

                                                }

                                            }
                                        }

                                    }

                                }

                                for (routine in routines) {
                                    for (temi18Rou in routines[routine].temi18RouQues) {

                                        if (!routines[routine].temi18RouQues[temi18Rou].temi15UteQue)
                                            routines[routine].temi18RouQues
                                                .splice(
                                                    temi18Rou,
                                                    1);
                                    }
                                }

                                $rootScope.routines = routines;

                                if ($rootScope.ertaQrGuiUser.admin) {

                                    const newRoutine = angular
                                        .copy(constant.NEW_ROUTINE);

                                    $rootScope.routines
                                        .push(newRoutine);
                                }

                                HtmlUtilityFactory[templateSetFunction]
                                ($scope,
                                    $rootScope.routines);
                            });

                };

                /**
                 * init
                 */
                getRoutines();

                /**
                 * Crea routine
                 */
                routineController.newRoutine = function (routine) {

                    EmiaRestUtilityFactory.PostRoutine(routine).then(
                        function (response) {

                            routine.rou = response.entity.rou;
                            routine.insRou = response.entity.insRou;

                            ListUtilityFactory.NewPanel($scope,
                                $rootScope.routines,
                                constant.NEW_ROUTINE,
                                templateSetFunction);

                            routineController.messageSuccess = [];

                            routineController.messageSuccess
                                .push('Routine ' + routine.des
                                    + ' creata con successo!');

                        },
                        function (response) {
                            UtilErrorsFactory.SetErrors(
                                routineController,
                                response.errorMessage);
                        }, function (value) {

                        });

                };

                /**
                 * Cancella routine
                 */
                routineController.deleteRoutine = function (routine, index) {

                    const entity = {
                        name: routine.des,
                        description: 'La routine verrà cancellata. Vuoi continuare?',
                        action: 'Cancella Routine'
                    };

                    /*
                     * Modale per cancellare una entityà
                     */

                    const modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'views/modal/deleteEntityModal.html?v1.2.1',
                        controller: 'DeleteModalCtrl as deleteModal',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return entity;
                            }
                        },
                        backdrop: 'static'
                    });

                    modalInstance.result
                        .then(
                            function (message) {

                                EmiaRestUtilityFactory
                                    .DeleteRoutine({rou: routine.rou, insRou: routine.insRou})
                                    .then(
                                        function (value) {

                                            routineController.messageSuccess = [];

                                            routineController.messageSuccess
                                                .push(message);

                                            getRoutines();

                                        },
                                        function (response) {
                                            UtilErrorsFactory
                                                .SetErrors(
                                                    routineController,
                                                    response.errorMessage);
                                        }, function (value) {

                                        });
                            }, function () {

                            });

                };

                /**
                 * Quando dispongo la lista sul display, se è l'ultima avrà
                 * la possibilità di creare una nuova routine
                 */
                routineController.checkLast = function (index, row, factor) {

                    return ListUtilityFactory.CheckLast(index, row, factor,
                        $rootScope.routines);

                };

                /**
                 * Drag & drop della query
                 */
                routineController.onQueryDrop = function (rout, $data,
                                                          $index) {

                    EmiaRestUtilityFactory.PostRoutineQuery(rout.rou,
                        rout.insRou, $data.que, $data.insQue)
                        .then(
                            function (response) {

                                getRoutines();

                            },
                            function (response) {
                                UtilErrorsFactory.SetErrors(
                                    routineController,
                                    response.errorMessage);
                            }, function (value) {

                            });

                };

                /**
                 * Eseguo tutte le query contenute nella routine, rimanda
                 * alla stessa operazione di eseguire una query da categoria
                 */
                routineController.executeRoutine = function (routine) {
                    console.log("eseguo routine");

                    const queryToJson = [];

                    for (const query in routine.temi18RouQues) {
                        queryToJson
                            .push({
                                query: routine.temi18RouQues[query].temi15UteQue,
                                json: JSON
                                    .parse(routine.temi18RouQues[query].temi15UteQue.json)
                            });
                    }

                    openModal('lg', angular.copy(queryToJson));
                };

                /*
                 * Modale per il risultato delle query associate alla
                 * routine
                 */
                var openModal = function (size, list) {

                    const modalInstance = $uibModal
                        .open({
                            animation: true,
                            templateUrl: 'views/routinesResult.html?v1.2.1',
                            controller: 'RoutineModalResultsCtrl as routineModalResults',
                            size: size,
                            resolve: {
                                queries: function () {
                                    return list;
                                }
                            },
                            backdrop: 'static'
                        });

                    modalInstance.result.then(function (message) {

                    }, function () {

                    });
                };

                /**
                 * Cancello l'associazione di una query alla routine
                 */
                routineController.deleteQueryRoutine = function (query, rout) {

                    const entity = {
                        name: query.temi15UteQue.nam,
                        description: 'La query verrà cancellata da '
                            + rout.des + '. Vuoi continuare?',
                        action: 'Cancella Query dalla routine'
                    };

                    /*
                     * Modale per cancellare una entity
                     */

                    const modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'views/modal/deleteEntityModal.html?v1.2.1',
                        controller: 'DeleteModalCtrl as deleteModal',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return entity;
                            }
                        },
                        backdrop: 'static'
                    });

                    modalInstance.result.then(function (message) {

                        EmiaRestUtilityFactory.DeleteRoutineQuery(
                            rout.rou, rout.insRou, query.id.que,
                            query.id.insQue).then(
                            function (value) {

                                routineController.messageSuccess = [];

                                routineController.messageSuccess
                                    .push(message);
                                getRoutines();

                            },
                            function (response) {
                                UtilErrorsFactory.SetErrors(
                                    routineController,
                                    response.errorMessage);
                            }, function (value) {

                            });
                    }, function () {

                    });

                };

                routineController.closeAlert = function (index) {
                    routineController.messageSuccess = [];
                };
            }]);