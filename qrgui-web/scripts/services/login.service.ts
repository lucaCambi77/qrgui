import angular from 'angular';

(function () {
	'use strict';

	angular.module('qrGuiApp').factory('LoginFactory', LoginFactory);

	LoginFactory.$inject = ['$rootScope', 'RestFactory', 'DateUtilityFactory',
		'$location', 'EmiaRestUtilityFactory', '$q', 'UtilErrorsFactory',
		'ListUtilityFactory', 'constant', '$window', '$http']
	function LoginFactory($rootScope, RestFactory, DateUtilityFactory,
		$location, EmiaRestUtilityFactory, $q, UtilErrorsFactory,
		ListUtilityFactory, constant, $window, $http) {

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

						$rootScope.columnOffSet = "col-md-offset-1";

						var user = response[1].entity;
						$rootScope.ertaQrGuiUser = user;

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