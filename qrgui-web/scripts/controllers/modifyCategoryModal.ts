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
				'ModifyCategory', ['categ', '$uibModalInstance', 'EmiaRestUtilityFactory',
					'UtilErrorsFactory',
				function(categ, $uibModalInstance, EmiaRestUtilityFactory,
						UtilErrorsFactory) {

					var modifyCategoryModal = this;

					modifyCategoryModal.errors = [];

					modifyCategoryModal.categ = categ;

					var queryList = [];

					EmiaRestUtilityFactory
							.GetQueriesByTipCateg(
									categ.root.temi20AnaTipCat.tipCat,
									categ.queries)
							.then(
									function(response) {

										modifyCategoryModal.queries = [];

										for ( var element in response.entity) {

											modifyCategoryModal.queries
													.push({
														query : response.entity[element],
														checked : false

													})

										}

									},
									function(response) {
										UtilErrorsFactory.SetErrors(
												modifyCategoryModal,
												response.errorMessage);
									}, function(value) {

									})

					modifyCategoryModal.save = function() {

						var queryToAdd = [];

						for ( var el in modifyCategoryModal.queries) {

							if (modifyCategoryModal.queries[el].checked) {
								var obj = {
									id : {
										cat : categ.root.id.cat,
										insCat : categ.root.id.insCat,
										que : modifyCategoryModal.queries[el].query.id.que,
										insQue : modifyCategoryModal.queries[el].query.id.insQue
									},
									temi15UteQue : null,
									temi14UteCat : null
								}

								queryToAdd.push(obj)
							}
						}

						EmiaRestUtilityFactory
								.PostQueCatAssoc(queryToAdd)
								.then(
										function(response) {

											$uibModalInstance
													.close("Queries aggiunte a categoria "
															+ categ.root.des)
										},
										function(response) {
											UtilErrorsFactory.SetErrors(
													modifyCategoryModal,
													response.errorMessage);

										}, function(value) {

										})
					}

					modifyCategoryModal.cancel = function() {
						$uibModalInstance.dismiss('cancel');

					};

					modifyCategoryModal.queryCheck = function(query) {

						var queryToCheck = $
								.grep(
										modifyCategoryModal.queries,
										function(e : any) {
											return (e.query.id.que == query.id.que && e.query.id.insQue == query.id.insQue);
										})[0];

						queryToCheck.checked = !queryToCheck.checked;
					}
				}])