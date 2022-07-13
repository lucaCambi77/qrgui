/**
 * 
 */
 import angular from 'angular';

 angular.module('qrGuiApp').factory('EmiaRestUtilityFactory', ['constant',
			'RestUtilityFactory', 'ModelFactory', 'UtilErrorsFactory',
			'$rootScope', '$q',
	function EmiaRestUtilityFactory(constant, RestUtilityFactory,
			ModelFactory, UtilErrorsFactory, $rootScope, $q) {

		var restUtilService : any =  {};

		restUtilService.GetDataBaseInfo = GetDataBaseInfo;

		restUtilService.PostCategory = PostCategory;
		restUtilService.GetCategories = GetCategories;
		restUtilService.DeleteCategory = DeleteCategory;

		restUtilService.PostQueCatAssoc = PostQueCatAssoc;
		restUtilService.GetQueCatAssoc = GetQueCatAssoc;

		restUtilService.GetQueryByDb = GetQueryByDb;
		restUtilService.GetQueryById = GetQueryById;
		restUtilService.PostQuery = PostQuery;
		restUtilService.GetQueriesByTipCateg = GetQueriesByTipCateg;
		restUtilService.GetAlreadyAssociatedQuery = GetAlreadyAssociatedQuery;
		restUtilService.DeleteQuery = DeleteQuery;

		restUtilService.GetRoutines = GetRoutines;
		restUtilService.PostRoutine = PostRoutine;
		restUtilService.DeleteRoutine = DeleteRoutine;
		restUtilService.PostRoutineQuery = PostRoutineQuery;
		restUtilService.DeleteRoutineQuery = DeleteRoutineQuery;

		restUtilService.GetAllowedCategories = GetAllowedCategories;

		restUtilService.GetQueryByDbWrapper = GetQueryByDbWrapper;

		return restUtilService;

		function GetQueryByDbWrapper(controller) {

			if (null != $rootScope.selectedSchema) {

				var list = [ GetQueryByDb($rootScope.selectedSchema.id.csch,
						$rootScope.selectedSchema.id.ctyp) ];

				$q
						.allSettled(list)
						.then(
								function(response) {
									var hasErrors = UtilErrorsFactory
											.CheckResponse(controller, response).hasErrors;

									$rootScope.schema.queries[$rootScope.selectedSchema.id.csch] = [];

									/*
									 * Se l'item è visible , faccio il check
									 * sulla lista delle query dello schema
									 */
									for ( var element in response[0].value.entity) {

										var item = response[0].value.entity[element];

										for ( var schemaQueries in $rootScope.selects[$rootScope.selectedSchema.id.csch]) {

											if ($rootScope.selects[$rootScope.selectedSchema.id.csch][schemaQueries].isVisible
													&& $rootScope.selects[$rootScope.selectedSchema.id.csch][schemaQueries].cque == response[0].value.entity[element].cque)
												item.isOnBoard = true;
										}

										$rootScope.schema.queries[$rootScope.selectedSchema.id.csch]
												.push(item);

									}

								});
			}

		}

		function GetAllowedCategories() {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/anaTipCat';

			return RestUtilityFactory.DeferredPromiseGet(path);
		}

		function GetDataBaseInfo() {

			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/dbInfo';

			return RestUtilityFactory.DeferredPromiseGet(path);

		}

		function GetCategories() {

			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/category';

			return RestUtilityFactory.DeferredPromiseGet(path);

		}

		function PostQueCatAssoc(queryToAdd) {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/queCatAssoc/post';

			return RestUtilityFactory.DeferredPromisePost(path, queryToAdd);
		}

		function GetQueCatAssoc() {

			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/queCatAssoc';

			return RestUtilityFactory.DeferredPromiseGet(path);

		}

		function PostCategory(parent, newCateg) {

			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/category';

			return RestUtilityFactory.DeferredPromisePost(path, ModelFactory
					.GetTemi14(parent, newCateg), null);
		}

		function DeleteCategory(category) {

			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/category/delete/';

			return RestUtilityFactory.DeferredPromisePost(path, category);
		}

		function DeleteQuery(queId) {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/query/delete/';

			return RestUtilityFactory.DeferredPromisePost(path, queId, {});

		}

		function PostQuery(ttps15) {

			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/query';

			return RestUtilityFactory.DeferredPromisePost(path, ttps15);
		}

		function GetQueryByDb(schema, type) {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/query/db';

			return RestUtilityFactory.DeferredPromiseGet(path, {
				schema : schema,
				type : type
			});
		}

		function GetQueryById(cque) {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/query/' + cque;

			return RestUtilityFactory.DeferredPromiseGet(path);
		}

		function GetQueriesByTipCateg(tipCat, queries) {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/query/tipCateg';

			return RestUtilityFactory.DeferredPromisePost(path, queries, {
				tipCat : tipCat,
			});
		}

		function GetAlreadyAssociatedQuery(ccat, insCat, tipCat) {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/query/associatedQuery';

			return RestUtilityFactory.DeferredPromiseGet(path, {
				cat : ccat,
				tipCat : tipCat,
				insCat : insCat
			});
		}

		function GetRoutines() {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/routine';

			return RestUtilityFactory.DeferredPromiseGet(path, {});
		}

		function PostRoutine(routine) {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/routine';

			return RestUtilityFactory.DeferredPromisePost(path, routine);
		}

		function DeleteRoutine(routine) {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/routine/delete';

			return RestUtilityFactory.DeferredPromisePost(path, routine);
		}

		function PostRoutineQuery(rou, insRou, que, insQue) {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/routQuery';

			var data = ModelFactory.GetTemi18Pk(rou, insRou, que, insQue);

			return RestUtilityFactory.DeferredPromisePost(path, data);
		}

		function DeleteRoutineQuery(rou, insRou, que, insQue) {
			var path = constant.contextRoot + constant.restBasicPath
					+ '/emia/routQuery/delete';

			return RestUtilityFactory.DeferredPromisePost(path, {
				rou : rou,
				insRou : insRou,
				que : que,
				insQue : insQue
			});
		}

}])