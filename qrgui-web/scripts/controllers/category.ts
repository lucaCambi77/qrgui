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
		'CategoryCtrl', ['HtmlUtilityFactory', '$scope', '$rootScope', 'constant',
		'ListUtilityFactory', '$uibModal', 'EmiaRestUtilityFactory',
		'$q', 'UtilErrorsFactory',
		function (HtmlUtilityFactory, $scope, $rootScope, constant,
			ListUtilityFactory, $uibModal, EmiaRestUtilityFactory,
			$q, UtilErrorsFactory) {

			var category = this;

			category.errors = [];

			category.messageSuccess = [];

			category.addQueryTitle = 'Aggiungi Query';
			category.deleteCategoryTitle = 'Cancella Categoria';
			category.addCategoryTitle = 'Aggiungi Categoria';
			category.modifyCategoryTitle = 'Modifica Categoria';

			category.executeQueryTitle = 'Esegui Query';
			category.deleteQueryTitle = 'Cancella Query';
			category.modifyQueryTitle = 'Modifica Query';

			var templateSetFunction = 'SetCategoriesListTemplate';

			HtmlUtilityFactory[templateSetFunction]($scope,
				$rootScope.categories);

			/**
			 * Nuova categoria, anche come sottocategoria
			 */
			category.newCategory = function (parent, newCateg) {

				if (null == newCateg.root.des
					|| null == newCateg.root.temi20AnaTipCat.tipCat) {
					category.errors
						.push('Nome Categoria o Tipo Categoria mancanti');
					return;
				}

				var list = [
					EmiaRestUtilityFactory.PostCategory(parent,
						newCateg),
					EmiaRestUtilityFactory.GetQueCatAssoc()];

				$q
					.all(list)
					.then(
						function (response) {
							if (UtilErrorsFactory
								.CheckResponse(category,
									response).hasErrors)
								return;

							/**
							 * categorie con alberatura
							 */
							$rootScope.categories = response[0].entity;

							ListUtilityFactory
								.SetCategoriesTree(
									$rootScope.categories,
									response[1].entity);

							ListUtilityFactory.NewPanel($scope,
								$rootScope.categories,
								constant.NEW_CATEGORY,
								templateSetFunction);

							category.errors = [];

							category.messageSuccess = [];

							category.messageSuccess
								.push('Categoria '
									+ newCateg.root.des
									+ ' creata con successo!');

						});

			}

			/**
			 * Funzione Crea sottocategoria
			 */
			category.createSubCategory = function (item, data) {

				category.errors = [];

				if (item.root.temi20AnaTipCat.tipCat != data.root.temi20AnaTipCat.tipCat) {
					category.errors
						.push('La Sotto Categoria non è dello stesso tipo della Categoria');
					return;
				}

				category.newCategory(item, data);

			}

			/**
			 * Cancella query da categoria
			 */
			category.deleteQuery = function (query, categ) {

				var entity = {
					name: query.nam,
					description: 'La query verrà rimossa da tutte le categorie e routine in cui si trova e poi cancellata definitivamente. Vuoi continuare?',
					action: 'Cancella Query'
				}

				/*
				 * Modale per cancellare una entityà
				 */
				var modalInstance = $uibModal.open({
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
								.DeleteQuery(query.que, query.insQue)
								.then(
									function (value) {

										ListUtilityFactory
											.LoadCategoriesState(category);

										HtmlUtilityFactory[templateSetFunction]
											(
												$scope,
												$rootScope.categories);

										category.messageSuccess = [];

										category.messageSuccess
											.push(message);

									},
									function (response) {
										UtilErrorsFactory
											.SetErrors(
												category,
												response.errorMessage);
									}, function (value) {

									})
						}, function () {

						});

			}

			/**
			 * Cancella categoria. Per le query contenute e non
			 * associate ad altre categorie, si richiede una eventuale
			 * riassociazione
			 */
			category.deleteCategory = function (cat) {
				EmiaRestUtilityFactory
					.GetAlreadyAssociatedQuery(cat.root.cat,
						cat.root.insCat,
						cat.root.temi20AnaTipCat.tipCat)
					.then(
						function (response) {

							category.errors = [];

							var notAssociatedQueries = [];

							for (var element in response.entity) {
								notAssociatedQueries
									.push(response.entity[element])
							}

							var entity = {
								category: cat,
								description: 'Le query già associate ad altre categorie verranno rimosse da questa categoria.'
									+ ' Per le queries non già associate, prego specificare un\' altra categoria, altrimenti saranno cancellate.',
								descriptionDelete: 'Verrà cancellata la categoria '
									+ cat.root.des
									+ ' e tutte le sottocategorie. Vuoi continuare?',
								action: 'Cancella Categoria',
								queries: notAssociatedQueries
							}
							/*
							 * Modale per cancellare una entityà
							 */

							var modalInstance = $uibModal
								.open({
									animation: true,
									templateUrl: 'views/modal/deleteCategoryModal.html?v1.2.1',
									controller: 'DeleteCategoryModalCtrl as deleteCategoryModal',
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

										category.messageSuccess = [];

										category.messageSuccess
											.push(message);

										ListUtilityFactory
											.LoadCategoriesState(category);

										HtmlUtilityFactory[templateSetFunction]
											(
												$scope,
												$rootScope.categories);

									}, function () {

									});
						},
						function (response) {
							UtilErrorsFactory.SetErrors(
								category,
								response.errorMessage);
						}, function (value) {

						})

			}

			/**
			 * Controlla se è l'ultima categoria, nel caso si
			 * visualizzano i bottoni per crearne una nuova
			 */
			category.CheckLast = function (index, row, factor) {

				return ListUtilityFactory.CheckLast(index, row, factor,
					$rootScope.categories);

			}

			/**
			 * Esegui query da categoria, la stessa funzionalità delle
			 * routine
			 */
			category.executeQueryFromCat = function (query) {
				console.log("eseguo query");

				var queryToJson = [{
					query: query,
					json: JSON.parse(query.json)
				}];

				openModal('lg', queryToJson);
			}

			/*
			 * Modale per il risultato delle query associate alla
			 * routine
			 */
			var openModal = function (size, list) {

				var modalInstance = $uibModal
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
			 * Aggiungi una query ad una categoria
			 */
			category.addQuery = function (categ) {

				var queryObject = {
					query: null,
					categ: categ

				}

				var modalInstance = $uibModal
					.open({
						animation: true,
						templateUrl: 'views/modal/addQueryToCategories.html',
						controller: 'AddQueryToCategory as addQueryToCategory',
						size: 'lg',
						resolve: {
							queryObject: function () {
								return queryObject;
							}
						},
						backdrop: 'static'
					});

				modalInstance.result.then(function (message) {

					category.messageSuccess = [];

					category.messageSuccess.push(message);

					ListUtilityFactory.LoadCategoriesState(category);

					HtmlUtilityFactory[templateSetFunction]($scope,
						$rootScope.categories);

				}, function () {

				});
			}

			/**
			 * Aggiorna query di una categoria // TODO fare una sola
			 * funziona con l'addQuery
			 */
			category.updateCategoryQuery = function (query, categ) {

				var queryObject = {
					query: query,
					categ: categ

				}
				var modalInstance = $uibModal
					.open({
						animation: true,
						templateUrl: 'views/modal/addQueryToCategories.html?v1.2.1',
						controller: 'AddQueryToCategory as addQueryToCategory',
						size: 'lg',
						resolve: {
							queryObject: function () {
								return queryObject;
							}
						},
						backdrop: 'static'
					});

				modalInstance.result.then(function (message) {

					category.messageSuccess = [];

					category.messageSuccess.push(message);

					ListUtilityFactory.LoadCategoriesState(category);

					HtmlUtilityFactory[templateSetFunction]($scope,
						$rootScope.categories);

				}, function () {

				});
			}

			/**
			 * Modifica Categoria, per il momento solo per
			 * l'associazione di altre query dello stesso tipo
			 */
			category.modifyCategory = function (categ) {

				var modalInstance = $uibModal
					.open({
						animation: true,
						templateUrl: 'views/modal/modifyCategory.html?v1.2.1',
						controller: 'ModifyCategory as modifyCategoryModal',
						size: 'sm',
						resolve: {
							categ: function () {
								return categ;
							}
						},
						backdrop: 'static'
					});

				modalInstance.result.then(function (message) {

					category.messageSuccess = [];

					category.messageSuccess.push(message);

					ListUtilityFactory.LoadCategoriesState(category);

					HtmlUtilityFactory[templateSetFunction]($scope,
						$rootScope.categories);

				}, function () {

				});
			}

			category.closeAlert = function (index) {
				category.messageSuccess = [];
			};

		}])