'use strict';
import angular from 'angular';

/**
 * @ngdoc function
 * @name qrGuiApp.controller:MainCtrl
 * @description # MainCtrl Controller of the qrGuiApp
 */
angular.module('qrGuiApp').controller(
		'DeleteModalCtrl', [ 'entity', '$uibModalInstance',
		function(entity, $uibModalInstance) {

			var deleteModal = this;

			deleteModal.entity = entity;

			deleteModal.deleteEntity = function() {

				$uibModalInstance.close(entity.name
						+ " cancellata con successo")
			}

			deleteModal.cancel = function() {
				$uibModalInstance.dismiss('cancel');

			};
		}])