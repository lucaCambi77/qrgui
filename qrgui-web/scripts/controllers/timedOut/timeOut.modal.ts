'use strict';

import angular from 'angular';

/**
 * @ngdoc overview
 * @name obuApp
 * @description # obuApp
 * 
 * Main module of the application.
 */
angular.module('qrGuiApp').controller('TimedOutCtrl',
		function TimedOutCtrl($uibModalInstance) {

			var timedOut = this;

			timedOut.loginOutOk = function() {
				$uibModalInstance.close();
			};
		})
