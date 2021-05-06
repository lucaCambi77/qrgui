/**
 * 
 */
import angular from 'angular';

(function() {
	'use strict';

	angular.module('qrGuiApp').factory('RestUtilityFactory',
			RestUtilityFactory);

	RestUtilityFactory.$inject = [ '$rootScope', '$q', '$http', 'constant' ];
	function RestUtilityFactory($rootScope, $q, $http, constant) {

		var restUtilService : any = {};

		restUtilService.DeferredPromiseGet = DeferredPromiseGet;
		restUtilService.DeferredPromisePost = DeferredPromisePost;
		restUtilService.DeferredPromisePut = DeferredPromisePut;
		restUtilService.DeferredPromiseDelete = DeferredPromiseDelete;

		return restUtilService;

		function DeferredPromiseGet(path, params?) {
			var defer = $q.defer();

			$http.get($rootScope.restdev + path, {
				params : params == null ? {} : params,
				headers : constant.JSONCONTENTTYPE
			}).then(function(response) {
				defer.resolve(response.data);
			}, function(response) {
				defer.reject(response.data);
			}, function(value) {

			});

			return defer.promise;
		}

		function DeferredPromisePost(path, data, params?) {
			var defer = $q.defer();

			var req = {
				method : 'POST',
				url : $rootScope.restdev + path,
				headers : constant.JSONCONTENTTYPE,
				data : data,
				params : params == null ? {} : params,
			}

			$http(req).then(function(response) {
				defer.resolve(response.data);
			}, function(response) {
				defer.reject(response.data);
			}, function(value) {

			});

			return defer.promise;

		}

		function DeferredPromisePut(path, data) {
			var defer = $q.defer();

			$http.put($rootScope.restdev + path, data, constant.JSONCONTENTTYPE).then(
					function(response) {
						defer.resolve(response.data);
					}, function(response) {
						defer.reject(response.data);
					}, function(value) {

					});

			return defer.promise;
		}

		function DeferredPromiseDelete(path, data, params?) {
			var defer = $q.defer();

			var req = {
				method : 'DELETE',
				url : $rootScope.restdev + path,
				headers : constant.JSONCONTENTTYPE,
				data : data,
				params : params == null ? {} : params,
			}

			$http(req).then(function(response) {
				defer.resolve(response.data);
			}, function(response) {
				defer.reject(response.data);
			}, function(value) {

			});

			return defer.promise;

		}
	}

})();