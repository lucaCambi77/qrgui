import angular from 'angular';

(function () {
	'use strict';

	angular.module('qrGuiApp').factory('LoginFactory', LoginFactory);

	LoginFactory.$inject = ['$rootScope', 'RestFactory', 'DateUtilityFactory',
		'$location', 'EmiaRestUtilityFactory', '$q', 'UtilErrorsFactory',
		'ListUtilityFactory', '$window', '$http']
	function LoginFactory($rootScope, RestFactory, DateUtilityFactory,
		$location, EmiaRestUtilityFactory, $q, UtilErrorsFactory,
		ListUtilityFactory, $window, $http) {

		var service: any = {};

		service.GetUserProperties = GetUserProperties;

		return service;

		function GetUserProperties(input) {

			console.log("... verifico autenticazione... ");

			var token
				= $window.btoa(input.username + ':' + input.password);

			$http.defaults.headers.common['Authorization']
				= 'Basic ' + token;

			var data = {
				userName: input.username,
				password: input.password,
				locale: input.locale,
				url: input.absUrl
			}

			var list = [EmiaRestUtilityFactory.GetDataBaseInfo(),
			RestFactory.GetUserProperties(data)];

			$q
				.all(list)
				.then(
					function (response) {

						if (UtilErrorsFactory.CheckResponse(input,
							response).hasErrors)
							return;

						console.log("l'utente è autenticato... ");

						$window.sessionStorage.setItem(
							'databaseInfoList', JSON.stringify(response[0].entity)
						);

						$rootScope.databaseInfoList = response[0].entity;

						$rootScope.columnOffSet = "col-md-offset-1";

						$rootScope.lastLogInDay = DateUtilityFactory
							.GetStringDateDay(new Date());

						ListUtilityFactory.LoadCategoriesState(input);

						var userData = {
							userName: input.username,
							authData: token
						}

						$window.sessionStorage.setItem(
							'userData', JSON.stringify(userData)
						);

						var user = response[1].entity;
						$rootScope.ertaQrGuiUser = user;

						$window.sessionStorage.setItem(
							'qruiUser', JSON.stringify(user)
						);

						$rootScope.isUserLogged = true;

						$location.path('/overview');
					}, function (response) {
						$location.path('/login');
					}, function (value) {
						console.log(value)
					});

		}

	}

})();