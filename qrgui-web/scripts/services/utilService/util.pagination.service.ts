/**
 * 
 */
import angular from 'angular';
import * as _ from 'ng-lodash';

(function() {
	'use strict';

	angular.module('qrGuiApp').factory('PaginationUtilityFactory',
			PaginationUtilityFactory);

	function PaginationUtilityFactory() {
		var pagination : any = {};

		pagination.GetPager = GetPager;
		pagination.GetTotalPages = GetTotalPages;

		return pagination;

		// service implementation
		function GetPager(totalItems, currentPage, pageSize) {
			// default to first page
			currentPage = currentPage || 1;

			// default page size is 15
			pageSize = pageSize || 10;

			// calculate total pages
			var totalPages = GetTotalPages(totalItems, pageSize);

			var startPage, endPage;
			if (totalPages <= 10) {
				// less than 10 total pages so show all
				startPage = 1;
				endPage = totalPages;
			} else {
				// more than 10 total pages so calculate start and end pages
				if (currentPage <= 6) {
					startPage = 1;
					endPage = 10;
				} else if (currentPage + 4 >= totalPages) {
					startPage = totalPages - 9;
					endPage = totalPages;
				} else {
					startPage = currentPage - 5;
					endPage = currentPage + 4;
				}
			}

			// calculate start and end item indexes
			var startIndex = (currentPage - 1) * pageSize;
			var endIndex = startIndex + pageSize;

			// create an array of pages to ng-repeat in the pager control
			var pages = _.range(startPage, endPage + 1);

			// return object with all pager properties required by the view
			return {
				totalItems : totalItems,
				currentPage : currentPage,
				pageSize : pageSize,
				totalPages : totalPages,
				startPage : startPage,
				endPage : endPage,
				startIndex : startIndex,
				endIndex : endIndex,
				pages : pages
			};
		}

		/*
		 * Restituisce il numero totale di pagine a partire dal totale diviso la
		 * dimensione della pagina
		 */
		function GetTotalPages(totalItems, pageSize) {
			return Math.ceil(totalItems / pageSize);
		}

	}

})();