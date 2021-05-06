/**
 * 
 */
import angular from 'angular';
import * as moment from 'moment';

(function() {
	'use strict';

	angular.module('qrGuiApp').factory('DateUtilityFactory',
			DateUtilityFactory);

	function DateUtilityFactory() {

		var dateUtilService : any = {};

		dateUtilService.SetDatePicker = SetDatePicker;
		dateUtilService.GetStringDate = GetStringDate;
		dateUtilService.GetStringDateDay = GetStringDateDay;
		dateUtilService.GetStringDateHours = GetStringDateHours;
		dateUtilService.GetUtcDateStringFromLong = GetUtcDateStringFromLong;
		dateUtilService.GetLocalDateFromLong = GetLocalDateFromLong;
		dateUtilService.GetUTCDate = GetUTCDate;

		return dateUtilService;

		function SetDatePicker(datePopUpInstance) {

			datePopUpInstance.isOpen = false;
			datePopUpInstance.value = null;

			datePopUpInstance.openDatePicker = function($event, instance) {
				$event.preventDefault();
				$event.stopPropagation();

				datePopUpInstance.isOpen = !instance;
			};

			datePopUpInstance.dateOptions = {
				// dateDisabled : disabled,
				formatYear : 'yy',
				maxDate : new Date(2050, 31, 12),
				minDate : new Date(2000, 1, 1),
				startingDay : 1
			};

			datePopUpInstance.timePickerOptions = {
				showMeridian : false,
				defaultTime : "00:00:00",
				showSeconds : true
			};

			datePopUpInstance.formats = [ 'yyyy-MM-dd HH:mm:ss',
					'dd-MMMM-yyyy HH:mm:ss', 'dd.MM.yyyy', 'shortDate' ];
			datePopUpInstance.format = datePopUpInstance.formats[0];
			datePopUpInstance.altInputFormats = [ 'M!/d!/yyyy' ];

		}

		function GetStringDate(dateObj, separator) {

			var dateSeparator = null == separator ? "/" : separator;
			if (null != dateObj) {
				var monthDayYearPath = GetStringDateDay(dateObj, dateSeparator);

				var hrMinSecPath = GetStringDateHours(dateObj, dateSeparator);

				var newdate = monthDayYearPath + " " + hrMinSecPath;

				return newdate;
			} else {
				return null;
			}
		}

		function GetStringDateDay(dateObj, separator) {

			var dateSeparator = null == separator ? "/" : separator;
			var path : any = {};
			path.year = dateObj.getUTCFullYear();
			path.month = dateObj.getUTCMonth() + 1;
			path.day = dateObj.getDate();

			return path.year + dateSeparator + datePad(path.month)
					+ dateSeparator + datePad(path.day);

		}

		function GetStringDateHours(dateObj, separator) {

			var dateSeparator = null == separator ? "/" : separator;
			var path : any = {};
			path.hr = dateObj.getHours();
			path.min = dateObj.getMinutes();
			path.sec = dateObj.getSeconds();

			return datePad(path.hr) + ":" + datePad(path.min) + ":"
					+ datePad(path.sec);

		}

		function GetUTCStringDate(dateObj, separator, hours) {

			var hasHours = null == hours ? true : hours;

			var dateSeparator = null == separator ? "/" : separator;
			if (null != dateObj) {
				var newDate;

				var year = dateObj.getUTCFullYear();
				var month = dateObj.getUTCMonth() + 1; // months from 1-12
				var day = dateObj.getUTCDate();

				newDate = year + dateSeparator + datePad(month) + dateSeparator
						+ datePad(day);

				if (!hasHours)
					return newDate;

				var hr = dateObj.getUTCHours();
				var min = dateObj.getMinutes();
				var sec = dateObj.getSeconds();

				newDate += " " + datePad(hr) + ":" + datePad(min) + ":"
						+ datePad(sec);

				return newDate;
			} else {
				return null;
			}
		}

		function datePad(value) {
			if (value < 10) {
				return '0' + value;
			} else {
				return value;
			}
		}

		function GetUtcDateStringFromLong(dateLong, separator, hasHours) {

			if (null == dateLong)
				return null;

			var iniDateWithTimeZone = new Date(dateLong);

			return GetUTCStringDate(iniDateWithTimeZone, separator, hasHours);
		}

		function GetLocalDateFromLong(dateLong) {

			var dateLocal = new Date(dateLong);

			return GetStringDate(dateLocal, null);
		}

		/*
		 * restituisce una data in UTC da long o date
		 */
		function GetUTCDate(object) {

			var date = moment(object);

			if (!date.isValid)
				return null;

			date.isUTC = () => true;
			return date;
		}

		/*
		 * restituisce una data in UTC (a partire dalla mezzanotte) dalla data
		 * in input. Usata nel calendar widget per i firmware
		 */
		function GetMidnightUTCDate(date) {

			var date : any = GetUTCDate(date);

			date.setHours(0);
			date.setMinutes(0);
			date.setSeconds(0);
			date.setMilliseconds(0);

			return date;
		}

	}

})();