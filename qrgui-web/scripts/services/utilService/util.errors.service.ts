/**
 * 
 */
import angular from 'angular';

angular.module('qrGuiApp').factory('UtilErrorsFactory',[ '$rootScope' ,
	function UtilErrorsFactory($rootScope) {

		var errors : any = {};
		errors.SetError = SetError;
		errors.SetErrors = SetErrors;
		errors.UnsetErrors = UnsetErrors;
		errors.CheckResponse = CheckResponse;

		return errors;

		/*
		 * Push Single Error
		 */
		function SetError(controller, error) {

			if (controller['errors'] == null)
				controller['errors'] = [];

			controller['errors'].push(error);
			controller['showError'] = true;

		}

		/*
		 * Push Errors on single response
		 */
		function SetErrors(controller, errors) {

			for ( var element in errors) {

				var error = errors[element];

				SetError(controller, error);

			}

			if (controller['errors'] == null)
				controller['errors'] = [];

			if (controller['errors'].length > 0)
				controller['errors'] = $.unique(controller['errors']);
		}

		/*
		 * Push Errors on multiple response
		 */
		function SetAllErrors(controller, errors) {

			for ( var element in errors) {

				for ( var errorElement in errors[element]) {

					var error = errors[element][errorElement];

					SetError(controller, error);
				}

			}


			if (controller['errors'] == null)
				controller['errors'] = [];
			
			if (controller['errors'].length > 0)
				controller['errors'] = $.unique(controller['errors']);
		}

		function UnsetErrors(controller) {
			controller['showError'] = false;
			controller['errors'] = [];
		}

		/*
		 * Controlla la response nel caso di chiamata multipla ($q.allSettled).
		 * Controlla se ci sono chiamate rifiutate e quali. Nel caso setta gli
		 * errori del controller in input
		 */
		function CheckResponse(controller, response) {

			var hasErrors = false;

			var checkedResponse = {

				hasErrors : hasErrors
			};

			if (null == response)
				return checkedResponse;

			var errors = [];

			for ( var element in response) {
				if (null != response[element]
						&& response[element].state === "rejected") {
					errors.push(response[element].reason.errorMessage);
				}

				if (null != response['errorMessage']
						&& response['errorMessage'].length > 0) {
					errors.push(response['errorMessage']);
				}
			}

			if (errors.length > 0) {

				checkedResponse.hasErrors = true;

				SetAllErrors(controller, errors)
			}

			return checkedResponse;
		}


}])