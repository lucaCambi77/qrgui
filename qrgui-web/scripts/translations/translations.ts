/**
 * @ngdoc overview
 * @name qrGuiApp
 * @description # qrGuiApp
 * 
 * Main module of the application.
 */
import angular from 'angular';
import 'angular-translate';

angular
		.module(
				'translations',
				[ 'pascalprecht.translate', 'tmh.dynamicLocale', 'ngSanitize' ])
		.config(
				[
						'$translateProvider',
						'tmhDynamicLocaleProvider',
						function($translateProvider, tmhDynamicLocaleProvider) {
							$translateProvider
									.useMissingTranslationHandlerLog();

							$translateProvider
									.determinePreferredLanguage(function(
											$window) {
										// define a function to determine the
										// language
										// and return a language key
										var userLang = navigator.language

										sessionStorage.setItem("locale", "it");

										return "it";
										// if (userLang.indexOf("it") >= 0) {
										//
										// return "it";
										// } else {
										// return "en";
										// }

									});

							$translateProvider
									.useSanitizeValueStrategy('sanitizeParameters');

							/*
							 * Usato per leggere le traduzione da files json in
							 * questa cartella, nel formato es. locale-en o
							 * locale-it
							 */
							$translateProvider.useStaticFilesLoader({
								prefix : 'scripts/translations/locale-',// path
								// to
								// translations
								// files
								suffix : '.json'// suffix, currently- extension
							// of the translations
							});

							tmhDynamicLocaleProvider
									.localeLocationPattern('node_modules/angular-i18n/angular-locale_{{locale}}.js');

						} ]);
