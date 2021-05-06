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
				'DeleteCategoryModalCtrl',
				function(entity, $uibModalInstance, $rootScope,
						UtilErrorsFactory, EmiaRestUtilityFactory) {

					var deleteCategoryModal = this;

					deleteCategoryModal.temi16List = [];

					deleteCategoryModal.categories = angular
							.copy($rootScope.categories);

					/**
					 * Tolgo le categorie che non mi interessano
					 */
					var categoriesToDelete = $
							.grep(
									deleteCategoryModal.categories,
									function(e : any) {
										return (e.data == undefined
												|| e.data == entity.category.data || e.root.temi20AnaTipCat.tipCat != entity.category.root.temi20AnaTipCat.tipCat);
									});

					for ( var aCateg in categoriesToDelete) {

						deleteCategoryModal.categories
								.splice(deleteCategoryModal.categories
										.indexOf(categoriesToDelete[aCateg]), 1);
					}

					/**
					 * Creo una temi16 da aggiungere alle nuove associazioni da
					 * creare
					 */
					deleteCategoryModal.addQueryToCategory = function(item,
							query) {

						var aTemi16 = $
								.grep(
										deleteCategoryModal.temi16List,
										function(e : any) {
											return (e.id.cat == item.root.id.cat
													&& e.id.que == query.id.que && e.id.insQue == query.id.insQue);
										});

						if (aTemi16 != null && aTemi16.length > 0) {

							deleteCategoryModal.temi16List.splice(deleteCategoryModal.temi16List
									.indexOf(aTemi16), 1);

							return;
						}

						var obj = {
							id : {
								cat : item.root.id.cat,
								insCat : item.root.id.insCat,
								que : query.id.que,
								insQue : query.id.insQue
							},
							temi15UteQue : null,
							temi14UteCat : null
						}

						deleteCategoryModal.temi16List.push(obj);

					}

					deleteCategoryModal.entity = entity;

					deleteCategoryModal.deleteEntity = function() {
						deleteCategoryModal.errors = [];

						entity.category.root.temi16QueCatAsses = deleteCategoryModal.temi16List;

						EmiaRestUtilityFactory
								.DeleteCategory(entity.category.root)
								.then(
										function(value) {

											$uibModalInstance
													.close(entity.category.root.des
															+ " cancellata con successo")

										},
										function(response) {
											UtilErrorsFactory.SetErrors(
													deleteCategoryModal,
													response.errorMessage);
										}, function(value) {

										})

					}

					deleteCategoryModal.cancel = function() {
						$uibModalInstance.dismiss('cancel');

					};
				})