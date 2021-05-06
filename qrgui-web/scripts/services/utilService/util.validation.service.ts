/**
 * 
 * Libreria per la validazione di valori tramite regular expressions. Da notare
 * che in javascript i caratteri speciali (punto (.) , digits (d), ecc.) devono
 * essere preceduti da \\ (se indicano esattamente quel valore, altrimenti hanno
 * un altro senso, tipo il (.) significa qualsiasi valore), mentre di solito
 * serve solo \
 */
import angular from 'angular';

(function() {
	'use strict';

	angular.module('qrGuiApp').factory('ValidationUtilityFactory',
			ValidationUtilityFactory);

	function ValidationUtilityFactory() {

		var validation : any = {};

		validation.CheckLength = CheckLength;
		validation.CheckLengthInRange = CheckLengthInRange;
		validation.IsNumber = IsNumber;
		validation.IsFloat = IsFloat;
		validation.IsNumberRange = IsNumberRange;
		validation.isFloatRange = isFloatRange;
		validation.isValidUrl = isValidUrl;
		validation.isAlphaNumeric = isAlphaNumeric;
		validation.isValidIpAddress = isValidIpAddress;

		return validation;

		function CheckLength(valueIn, length) {

			if (null == valueIn)
				return false;

			var minLength = "^.{" + (length == null ? 1 : length) + ",}$";
			var value = new RegExp(minLength);

			if (value.exec(valueIn)) {
				return true;
			}

			return false;
		}

		function CheckLengthInRange(valueIn, start, end) {

			if (null == valueIn)
				return false;

			var lengthRange = "^.{" + start + "," + end + "}$";
			var value = new RegExp(lengthRange);

			if (value.exec(valueIn)) {
				return true;
			}

			return false;
		}

		function IsNumber(valueIn, zero) {

			if (null == valueIn || isNaN(valueIn))
				return false;

			var zeroCondition = (zero == null || zero == false) ? "1" : "0";
			var isNumber = "^-?[" + zeroCondition + "-9]\\d*$";
			var value = new RegExp(isNumber);

			if (value.exec(valueIn)) {
				return true;
			}

			return false;
		}

		function IsFloat(valueIn) {

			if (null == valueIn)
				return false;

			var isFloat = "^(-|\\+)?[0-9]\\d*(\\.\\d{1,6})?$";
			var value = new RegExp(isFloat);

			if (value.exec(valueIn)) {
				return true;
			}

			return false;
		}

		function isAlphaNumeric(valueIn) {

			if (null == valueIn)
				return false;

			var isAlphaNumeric = "^[a-z0-9]+$";
			var value = new RegExp(isAlphaNumeric, "i");

			if (value.exec(valueIn)) {
				return true;
			}

			return false;

		}

		function isValidUrl(valueIn) {

			if (null == valueIn)
				return false;

			var isUrl = "^(ftp|http|https):\/\/[^ \"]+$";
			var value = new RegExp(isUrl);

			if (value.exec(valueIn)) {
				return true;
			}

			return false;
		}

		function isValidIpAddress(valueIn) {

			if (null == valueIn)
				return false;

			var isIpAddress = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
			var value = new RegExp(isIpAddress);

			if (value.exec(valueIn)) {
				return true;
			}

			return false;

		}

		function IsNumberRange(value, rangeStart, rangeEnd, checkZero) {

			if (isNaN(value))
				return false;

			if (null == checkZero || checkZero == true) {
				if (!IsNumber(value, checkZero))
					return false;
			}

			if (value < rangeStart || value > rangeEnd)
				return false;

			return true;
		}

		function isFloatRange(value, rangeStart, rangeEnd) {

			if (!IsFloat(value))
				return false;

			if (value < rangeStart || value > rangeEnd)
				return false;

			return true;
		}
	}

})();