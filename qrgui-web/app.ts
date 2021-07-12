
/**
 * @ngdoc overview
 * @name qrGuiApp
 * @description # qrGuiApp
 * 
 * Main module of the application.
 */
import angular from 'angular';

angular.module(
	'qrGuiApp',
	['translations', // module for translations, see
		// translations.js
		'ngCookies', 'ngResource', 'ngRoute', 'ngIdle', 'ui.bootstrap', 'ang-drag-drop']).config(['$routeProvider',
			'IdleProvider',
			function ($routeProvider, IdleProvider) {

				IdleProvider.idle(16200);
				IdleProvider.timeout(5);

				$routeProvider.when('/overview', {
					templateUrl: 'overview.html',
					controller: 'OverviewCtrl',
					controllerAs: 'overview'
				}).when('/about', {
					templateUrl: 'views/about.html',
					controller: 'AboutCtrl',
					controllerAs: 'about'
				}).when('/query', {
					templateUrl: 'views/doQuery.html',
					controller: 'QueryCtrl',
					controllerAs: 'query'
				}).when('/categories', {
					templateUrl: 'views/categories.html',
					controller: 'CategoryCtrl',
					controllerAs: 'category'
				}).when('/routines', {
					templateUrl: 'views/routines.html',
					controller: 'RoutineCtrl',
					controllerAs: 'routine'
				}).when('/login', {
					templateUrl: 'login.html',
					controller: 'LoginCtrl',
					controllerAs: 'auth',
					disableCache: true
				}).otherwise({
					redirectTo: '/login',
				});
			}]).run(
				['Idle', '$rootScope', 'constant',
					function (Idle, $rootScope, constant) {
						Idle.watch();
						/*
						 * Definisco le constanti come variabile di rootScope di
						 * modo da poterle utilizzare nelle pagine Html
						 */
						$rootScope.CONSTANT = constant;
					}]).controller('IndexController', ['$location', '$window', '$rootScope', 'constant', 'LoginFactory', '$uibModal', '$http', 'ListUtilityFactory',
						function IndexController($location, $window, $rootScope, constant, LoginFactory, $uibModal, $http, ListUtilityFactory) {

							console.log("sono nell'applicazione... ");

							$rootScope.ertaQrGuiUser = JSON.parse($window.sessionStorage.getItem('qruiUser'));

							$rootScope.restdev = process.env.NODE_ENV;

							var index = this;

							$rootScope.colorYellow = '#ecb941';
							$rootScope.colorRed = '#d84e4e';
							$rootScope.colorBlu = '#337ab7';
							$rootScope.colorGreen = '';

							$rootScope.desMaxLength = constant.DES_MAX_LENGTH;

							$rootScope.isUserLogged = false;
							$rootScope.columnOffSet = "col-md-offset-1";
							$rootScope.dateType = constant.GMT;

							index.absUrl = $location.$$host + ":" + $location.$$port
								+ constant.contextRoot;

							index.locale = sessionStorage.getItem("locale");

							var userData = $window.sessionStorage.getItem('userData');

							if (userData) {
								$http.defaults.headers.common['Authorization']
									= 'Basic ' + JSON.parse(userData).authData;
									ListUtilityFactory.LoadCategoriesState(index);
							} else {
								LoginFactory.GetUserProperties(index);
							}
							/*
							 * Controllo che l'utente non torni indietro alla pagina di login
							 */
							$rootScope.$watch(function () {
								return $location.path()
							}, function (newLocation, oldLocation) {
								if (newLocation.indexOf('login') > 0)
									$location.path(oldLocation);

							});

							/*
							 * Gestione idle. Viene visualizzato prima un count down e dopo una
							 * modale con reindirizzo forzato alla pagina di login
							 */
							// Idle section to check user session timeout
							function closeModals() {
								if ($rootScope.warning) {
									$rootScope.warning.close();
									$rootScope.warning = null;
								}

								/**
								 * Questo solo nel caso di un messaggio dopo il timeout
								 */
								if ($rootScope.timedout) {
									$rootScope.timedout.close();
									$rootScope.timedout = null;
								}
							}

							$rootScope.$on('IdleStart', function () {
								closeModals();

								$rootScope.warning = $uibModal.open({
									templateUrl: 'views/timeOut/warning-dialog.html',
									windowClass: 'modal-danger',
								});

							});

							$rootScope.$on('IdleEnd', function () {
								closeModals();
							});

							$rootScope.$on('IdleTimeout', function () {

								closeModals();
								index.logOut();

							});
						}]);
