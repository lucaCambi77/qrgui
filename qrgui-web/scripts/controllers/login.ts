import angular from 'angular';

/**
 * @ngdoc function
 * @name qrGuiApp.controller:LoginCtrl
 * @description # LoginCtrl Controller of the qrGuiApp
 */

(function () {
	'use strict';

	angular.module('qrGuiApp').controller('LoginCtrl', LoginCtrl);

	LoginCtrl.$inject = ['$rootScope', 'LoginFactory'];

	function LoginCtrl($rootScope, LoginFactory) {

		var auth = this;
		auth.login = Login;

		/*
		 * Login e Logout functions
		 */
		$rootScope.logOut = function () {

			sessionStorage.removeItem("isUserLogged");
			sessionStorage.removeItem("ertaQrGuiUser");
			$rootScope.isUserLogged = false;
			$rootScope.userName = null;
			$rootScope.password = null;

		}

		function Login() {

			LoginFactory.GetUserProperties({
				username: auth.username,
				password: auth.password,
				locale: auth.locale,
				url: auth.absUrl
			});

		}
	}

})();