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
		'RoutineModalResultsCtrl', [
		'queries', '$scope', '$compile', 'RestUtilityFactory',
		'UtilErrorsFactory', 'HtmlUtilityFactory', 'constant',
		'PaginationUtilityFactory',
		'$uibModalInstance', 'DateUtilityFactory',
		function (queries, $scope, $compile, RestUtilityFactory,
			UtilErrorsFactory, HtmlUtilityFactory, constant,
			PaginationUtilityFactory,
			$uibModalInstance, DateUtilityFactory) {

			var routineModal = this;

			routineModal.datePopup = {};

			routineModal.areResultsVisibile = false;

			for (var query in queries) {

				for (var attr in queries[query].json.attrs) {

					/**
					 * Per gli attributi data aggiungo il popup per poi
					 * toglierlo nell'esecuzione della query
					 */
					if (queries[query].json.attrs[attr].parameter.type == 'DATE'
						|| queries[query].json.attrs[attr].parameter.type == 'DATE_TRUNC') {

						queries[query].json.attrs[attr].parameter.datePopup = {};

						DateUtilityFactory
							.SetDatePicker(queries[query].json.attrs[attr].parameter.datePopup);

					}
				}
			}

			routineModal.queries = queries;

			/**
			 * Eseguo le query
			 */
			routineModal.doQueries = function (list, page,
				queryPosition, createFile) {

				var queryRestList = [];

				for (var element in list) {
					list[element].query.json = JSON.stringify(
						list[element].json, replacer)

					queryRestList.push(list[element].query);
				}

				RestUtilityFactory
					.DeferredPromisePost(
						constant.contextRoot
						+ constant.restBasicPath
						+ '/query/execute_query',
						queryRestList, {
						page: page,
						createFile: createFile
					})
					.then(
						function (response) {

							routineModal.errors = [];

							var hasErrors = UtilErrorsFactory
								.CheckResponse(
									routineModal,
									response.entity).hasErrors;

							/**
							 * Se ci sono errori li visualizzo
							 * nello spazio dove ci dovrebbe
							 * essere il risultato della query
							 */
							if (hasErrors) {
								$("#routineResults")
									.html(
										$compile(
											HtmlUtilityFactory
												.GetQueryErrorListTemplate(angular
													.copy(routineModal.errors)))
											($scope));
								return;
							}

							routineModal.areResultsVisibile = true;

							/*
							 * Faccio un template composto da
							 * tabella e paginatore
							 */

							var template = "";

							/**
							 * Se non ho la posizione allora sto
							 * lanciando tutte le query
							 */
							if (null == queryPosition) {

								routineModal.filePath = response.entity[0].queryFilePath;

								/**
								 * L'ordine di visualizzazione
								 * corrisponde all'ordine di
								 * esecuzione, questo forse non
								 * è l'ottimale ma è coerente.
								 * Altrimenti si dovrebbe
								 * prendere direttamente la
								 * query della response
								 */
								for (var element in response.entity) {

									var query = list[element].json;

									template += HtmlUtilityFactory
										.GetQueryListTemplate(
											response.entity[element].entity,
											query.querySelectColumns,
											element);

									// /* Paginatore */
									$scope['query' + element] = {};

									$scope['query' + element].pager = PaginationUtilityFactory
										.GetPager(
											response.entity[element].count,
											page);

									$("#routineResults")
										.append(
											$compile(
												addDivOnFirsLoad(
													element,
													template,
													response.entity[element].xentity.nam))
												(
													$scope));
									template = ""
								}

								/**
								 * Altrimenti ricarico la
								 * sezione della query richiesta
								 */
							} else {

								var query = list[queryPosition].json;

								template += HtmlUtilityFactory
									.GetQueryListTemplate(
										response.entity[0].entity,
										query.querySelectColumns,
										queryPosition);

								// /* Paginatore */
								$scope['query' + queryPosition] = {};

								$scope['query' + queryPosition].pager = PaginationUtilityFactory
									.GetPager(
										response.entity[0].count,
										page);

								$(
									"#routineResultList"
									+ queryPosition)
									.html(
										$compile(
											template)
											($scope));
							}

						},
						function (response) {

							routineModal.errors = [];
							UtilErrorsFactory.SetErrors(
								routineModal,
								response.entity.errorMessage);
						}, function (value) {
							console.log(value)

						})

			};

			$scope.submitQuery = function (page, queryPosition) {

				routineModal.doQueries(routineModal.queries, page,
					queryPosition, false);
			};

			function addDivOnFirsLoad(position, template, name) {
				var tmp = '<div id="routineResultList'
					+ position
					+ '">'
					+ '<h3 style="margin : 0.5em">'
					+ name
					+ '</h3>'
					+ '<p style="margin : 1em; color: #337ab7"></p>'
					+ template + '</div>';

				return tmp;

			}

			/**
			 * Eseguo le queries
			 */
			routineModal.executeQueries = function () {

				routineModal.doQueries(routineModal.queries, null,
					null, true);
			}

			/**
			 * Ritorno alla query dopo aver eseguito la routine
			 */
			routineModal.backToWhere = function () {
				routineModal.areResultsVisibile = false;

				$("#routineResults").html(
					$compile('<div></div>')($scope));
			}

			routineModal.cancel = function () {
				$uibModalInstance.dismiss('cancel');

			};

		}])

function replacer(key, value) {
	// if (key == "cque")
	// return undefined;
	// else
	if (key == "$$hashKey")
		return undefined;
	else if (key == "datePopup")
		return undefined;
	else if (key == "dateOptions")
		return undefined;
	else if (key == "timePickerOptions")
		return undefined;
	else if (key == "formats")
		return undefined;
	else if (key == "altInputFormats")
		return undefined;

	else
		return value;
}