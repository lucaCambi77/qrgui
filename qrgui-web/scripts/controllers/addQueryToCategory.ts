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
		'AddQueryToCategory', ['queryObject', '$uibModalInstance', '$scope', 'constant', 'ModelFactory',
		'EmiaRestUtilityFactory', 'UtilErrorsFactory',
		'RestUtilityFactory',
		function (queryObject, $uibModalInstance, $scope, constant, ModelFactory,
			EmiaRestUtilityFactory, UtilErrorsFactory,
			RestUtilityFactory) {

			var addQueryToCategory = this;

			addQueryToCategory.title = {};

			addQueryToCategory.title.collapseSection = "Collassa sezione destra per visuallizare meglio la query";

			/**
			 * Oggetto query in cui ci sono lo statement, gli attributi
			 * ed i constraint
			 */
			addQueryToCategory.query = {};

			/**
			 * Oggetto query in cui ci sono le informazioni dello schema
			 * ed il nome
			 */
			addQueryToCategory.queryObject = {};

			/**
			 * Recuper la categoria
			 */
			addQueryToCategory.categObject = angular
				.copy(queryObject.categ);

			addQueryToCategory.queryAttributes = [];
			addQueryToCategory.queryConstraints = [];

			addQueryToCategory.parameters = [];
			addQueryToCategory.queryAttrNames = [];

			if (null != queryObject.query) {

				addQueryToCategory.queryObject = angular
					.copy(queryObject.query);

				addQueryToCategory.query = angular.copy(ModelFactory
					.GetHtmlQueryFromTemi15(queryObject.query));

				for (var constraint in addQueryToCategory.query.constr) {

					addQueryToCategory.queryConstraints
						.push(addQueryToCategory.query.constr[constraint]);
				}
			}

			$scope.$watch("addQueryToCategory.query.statement",
				function (value) {

					if (null != addQueryToCategory.query)
						addQueryToCategory.parseQuery();
				});

			addQueryToCategory.saveQuery = function () {

				addQueryToCategory.errors = [];

				addQueryToCategory.query.attrs = addQueryToCategory.queryAttributes;
				addQueryToCategory.query.constr = addQueryToCategory.queryConstraints;

				addQueryToCategory.query.querySelectColumns = angular
					.copy(addQueryToCategory.querySelectColumnsTmp);

				var queryRest = ModelFactory.GetTemi15(
					addQueryToCategory.queryObject,
					addQueryToCategory.categObject.root,
					addQueryToCategory.query);

				RestUtilityFactory
					.DeferredPromisePost(
						constant.contextRoot
						+ constant.restBasicPath
						+ '/query/checkQuery',
						queryRest)
					.then(
						function (response) {
							var queryResponse = response.entity;

							EmiaRestUtilityFactory
								.PostQuery(queryRest)
								.then(
									function (value) {

										$uibModalInstance
											.close('Query '
												+ queryRest.nam
												+ ' '
												+ (queryRest.que == null ? 'creata'
													: ' modificata')
												+ ' con successo')

									},
									function (response) {

										UtilErrorsFactory
											.SetErrors(
												addQueryToCategory,
												response.errorMessage);

										// document
										// 		.querySelector(
										// 				'#'
										// 						+ 'focusCatch')
										// 		.focus();

									}, function (value) {

									})

						},
						function (response) {
							UtilErrorsFactory.SetErrors(
								addQueryToCategory,
								response.errorMessage);

							// document.querySelector(
							// 		'#' + 'focusCatch').focus();

						}, function (value) {

						})

			}

			addQueryToCategory.cancel = function () {
				$uibModalInstance.dismiss('cancel');

			};

			addQueryToCategory.parseQuery = function () {

				if (null == addQueryToCategory.query.statement)
					return;

				addQueryToCategory.errors = [];

				addQueryToCategory.queryAttributes = [];

				addQueryToCategory.parametersTmp = [];
				addQueryToCategory.parameters = [];
				addQueryToCategory.queryAttrNames = [];

				addQueryToCategory.querySelectColumnsTmp = [];
				/**
				 * Estraggo i parametri es. &P1
				 */
				var regex = new RegExp('&\\w+', 'g');

				var match;
				while (match = regex
					.exec(addQueryToCategory.query.statement)) {

					var queryOperator = null;

					addQueryToCategory.parametersTmp.push(match[0]);
					addQueryToCategory.parameters.push(match[0]);

					var result = addQueryToCategory.query.statement
						.split(match[0]);

					/**
					 * Tolgo i carriage return
					 */
					var splitSpace = result[0].replace(
						new RegExp('\n', 'g'), ' ').replace(
							new RegExp('\r', 'g'), ' ').replace(
								new RegExp('\t', 'g'), ' ').split(" ");

					var matched = false;

					/**
					 * Risalgo fino al nome dell'attributo
					 */
					for (var i = splitSpace.length - 1; i >= 0; i--) {
						var word = splitSpace[i];

						if (matched) {
							// var replace = word.replace(/^\(+|\)+$/g,
							// '');
							addQueryToCategory.queryAttrNames
								.push(word);
							matched = false;
							break;
						}

						/**
						 * Recupero l'operatore
						 */
						for (var operator in constant.COMPARISON) {
							if (constant.COMPARISON[operator] === word
								.toUpperCase()) {

								matched = true;
								queryOperator = constant.COMPARISON[operator];

							}
						}

					}

					/**
					 * Aggiungo i parametri e gli attributi
					 */
					var attribute = new ModelFactory.GetQueryAttribute();
					attribute.operator = queryOperator;
					attribute.attrName = word;

					attribute.parameter = new ModelFactory.GetParameter();
					attribute.parameter.name = match[0];

					/**
					 * Se esiste già quel parametro in update, allora lo
					 * riassocio al parametro
					 */
					for (var attr in addQueryToCategory.query.attrs) {

						if (match[0] == addQueryToCategory.query.attrs[attr].parameter.name
							&& addQueryToCategory.query.attrs[attr].attrName == word) {

							attribute.alias = addQueryToCategory.query.attrs[attr].alias;
							attribute.parameter.type = addQueryToCategory.query.attrs[attr].parameter.type;
						}
					}

					addQueryToCategory.queryAttributes.push(attribute);
				}

				addQueryToCategory.queryAttrNames = $
					.unique(addQueryToCategory.queryAttrNames);

				combinations(addQueryToCategory.parametersTmp);

				if (hasDuplicates(addQueryToCategory.parameters))
					addQueryToCategory.errors
						.push("Non si può definire lo stesso parametro più di una volta");
				/**
				 * Estraggo le colonne della query. Prendo la prima
				 * sezione di select -> from , tolgo le espressioni tra ()
				 * che possono contenere caratteri particolari, faccio
				 * lo spit per virgola(,) e recupero l'alias
				 */
				addQueryToCategory.attributesMatch = [];

				var regex = new RegExp('select(.*?)from');

				var attributesMatch = regex
					.exec(addQueryToCategory.query.statement
						.toLowerCase().replace(
							new RegExp('\n', 'g'), ' ')
						.replace(new RegExp('\r', 'g'), ' ')
						.replace(new RegExp('\t', 'g'), ' '))[0]
					.replace(new RegExp('select', 'g'), ' ')
					.replace(new RegExp('from', 'g'), ' ');
				//
				// var matches = attributesMatch.match(/\(([^)]+)\)/g);
				//
				// for ( var element in matches) {
				// attributesMatch = attributesMatch.replace(
				// matches[element], ' ')
				// }

				var splitComma = attributesMatch.split(",");

				for (var element in splitComma) {

					var splitAs = splitComma[element].split(" as ");

					var obj = {
						as: splitAs[1] == null ? splitAs[0].trim()
							: splitAs[1].trim(),
						type: null
					}

					/**
					 * Se esiste già quell'alias. gli riassocio il tipo
					 */
					for (var col in addQueryToCategory.query.querySelectColumns) {

						if (obj.as == addQueryToCategory.query.querySelectColumns[col].as
							.trim())
							obj.type = addQueryToCategory.query.querySelectColumns[col].type
					}

					// if (obj.as == null || obj.type == null)
					// addQueryToCategory.errors
					// .push('Alias o tipo mancante per la colonna '
					// + (parseInt(element) + 1));

					addQueryToCategory.querySelectColumnsTmp.push(obj);
				}

			}

			/**
			 * Aggiungi / rimuovi contraints e parametri
			 */
			addQueryToCategory.addAttributeParam = function () {

				var attribute = new ModelFactory.GetQueryAttribute();
				attribute.parameter = new ModelFactory.GetParameter();

				addQueryToCategory.queryAttributes.push(attribute);

			}

			addQueryToCategory.addQueryConstraint = function () {

				var constraint = new ModelFactory.GetConstraint();

				addQueryToCategory.queryConstraints.push(constraint);
			}

			addQueryToCategory.removeAttributeParam = function (
				attribute) {
				addQueryToCategory.queryAttributes.splice(
					addQueryToCategory.queryAttributes
						.indexOf(attribute), 1);
			}

			addQueryToCategory.removeConstraint = function (constraint) {
				addQueryToCategory.queryConstraints.splice(
					addQueryToCategory.queryConstraints
						.indexOf(constraint), 1);
			}

			/**
			 * Functions
			 */
			function hasDuplicates(array) {
				var valuesSoFar = Object.create(null);
				for (var i = 0; i < array.length; ++i) {
					var value = array[i];
					if (value in valuesSoFar) {
						return true;
					}
					valuesSoFar[value] = true;
				}
				return false;
			}

			/**
			 * Combinazioni di parametri a 2
			 */
			var combinations = function (array) {
				for (var i = 0; i < array.length; i++)
					for (var j = i + 1; j < array.length; j++)
						addQueryToCategory.parameters.push(array[i]
							+ ',' + array[j]);

			};
		}])