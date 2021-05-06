'use strict';
import angular from 'angular';

/**
 * @ngdoc function
 * @name qrGuiApp.controller:MainCtrl
 * @description # MainCtrl Controller of the qrGuiApp
 */
angular.module('qrGuiApp').controller('OverviewCtrl', ['$rootScope', '$location', '$window',
	function ($rootScope, $location, $window) {

		$rootScope.columnOffSet = "col-md-offset-1";

	}])