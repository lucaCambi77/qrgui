import angular from 'angular';

(function() {
	'use strict';

	angular.module('qrGuiApp').factory('RestFactory', RestFactory);

	RestFactory.$inject = [ 'constant', 'RestUtilityFactory' ];
	function RestFactory(constant, RestUtilityFactory) {
		var restService : any =  {};

		// Session
		restService.InvalidateSession = InvalidateSession;
		restService.GetUserCredential = GetUserCredential;
		restService.GetUserProperties = GetUserProperties;

		restService.Login = Login;

		return restService;

		// Error is already defined in rest response so there is no need to
		// handle response here ??

		// Session
		function InvalidateSession() {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/session/invalidate';
			return RestUtilityFactory.DeferredPromiseGet(path);
		}

		/*
		 * 
		 * @deprecated
		 */
		function GetUserCredential() {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/session/credential';
			return RestUtilityFactory.DeferredPromiseGet(path);
		}

		function Login(data) {
			console.log("faccio login call... ")
			var path = constant.contextRoot + constant.restBasicPath
					+ '/session/login';

			return RestUtilityFactory.DeferredPromisePost(path, data);
		}

		function GetUserProperties(data) {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/userProperties';

			return RestUtilityFactory.DeferredPromisePost(path, data);
		}

	}

})();
