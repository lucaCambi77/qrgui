/**
 * 
 */
import angular from 'angular';

angular.module('qrGuiApp').factory('ListUtilityFactory', ['constant', '$rootScope', 'UtilErrorsFactory', '$q', 'HtmlUtilityFactory', 'EmiaRestUtilityFactory',
	function ListUtilityFactory(constant, $rootScope, UtilErrorsFactory, $q, HtmlUtilityFactory, EmiaRestUtilityFactory) {

		var listUtil: any = {};

		listUtil.ConcatString = ConcatString;
		listUtil.GetMapKeyByValue = GetMapKeyByValue;
		listUtil.NewPanel = NewPanel;
		listUtil.CheckLast = CheckLast;
		listUtil.GetCategory = GetCategory;
		listUtil.SetCategoriesTree = SetCategoriesTree;
		listUtil.LoadCategoriesState = LoadCategoriesState;

		return listUtil;

		function LoadCategoriesState(controller) {
			/*
			 * Recupero le categorie, Aggiungo categoria nuova fittizia
			 */
			/*
			 * Recupero gli schema che vengono utilizzati dall'applicazione,
			 * associati nel backend al metadato jpa
			 */
			/*
			 * Recupero le query associate alle categorie
			 * 
			 */
			/*
			 * Recupero la lista dei tipi categoria
			 */
			var list = [EmiaRestUtilityFactory.GetCategories(),
			EmiaRestUtilityFactory.GetQueCatAssoc(),
			EmiaRestUtilityFactory.GetAllowedCategories()];

			$q.all(list).then(
				function (response) {
					if (UtilErrorsFactory.CheckResponse(controller,
						response).hasErrors)
						return;

					/**
					 * categorie con alberatura
					 */
					$rootScope.categories = response[0].entity;

					SetCategoriesTree($rootScope.categories,
						response[1].entity);

					if ($rootScope.ertaQrGuiUser.admin) {

						/**
						 * Creo la categoria di partenza per una nuova
						 */
						var newCategory = angular
							.copy(constant.NEW_CATEGORY);

						$rootScope.categories.push(newCategory);

					}

					$rootScope.tipCategories = response[2].entity;

				});
		}

		function SetCategoriesTree(categories, assoc) {

			/**
			 * per ogni categoria vado a caricare l'associazione con le queries
			 */
			for (var cat in categories) {

				for (var queCatAssoc in assoc) {

					var category = GetCategory(categories[cat],
						assoc[queCatAssoc].id.cat);

					if (category) {

						if (!category.queries)
							category.queries = [];

						category.queries.push(assoc[queCatAssoc].temi15UteQue)
					}
				}
			}
		}

		function GetCategory(category, id) {

			if (null == category.root.cat)
				return null;

			var item;

			if (category.data == id) {
				return category;
			}

			if (category.childrens.length > 0) {
				for (var i = 0; i < category.childrens.length; i++) {
					item = GetCategory(category.childrens[i], id);
					if (null != item)
						return item;
				}
			}
			return item;
		}

		function NewPanel($scope, listInput, newType, templateSetFunction) {

			if ($rootScope.ertaQrGuiUser.admin) {

				var newPanel = angular.copy(newType);

				listInput.push(newPanel);
			}

			HtmlUtilityFactory[templateSetFunction]($scope, listInput);

		}

		/**
		 * Controlla se un elemento di una lista è l'ultimo, serve per
		 * attribuire dinamicamente un attributo di creazione ad un pannello
		 */
		function CheckLast(index, row, factor, listInput) {

			/**
			 * Se non sono amministratore non posso avere la possibilità di
			 * aggiungere nuovi elementi
			 */
			if (!$rootScope.ertaQrGuiUser.admin)
				return false;

			var position = index + (factor * (row - 1));

			if (position == listInput.length - 1)
				return true;

			return false;

		}

		function ConcatString(list, operator) {
			if (null == list)
				return "";

			var finalString = list.join(operator);

			return finalString;
		}

		function GetMapKeyByValue(value, object) {
			for (var prop in object) {
				if (object.hasOwnProperty(prop)) {
					if (object[prop] === value)
						return prop;
				}
			}
		}


}]);