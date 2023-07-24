/**
 *
 */
import angular from 'angular';

angular.module('qrGuiApp').factory('EmiaRestUtilityFactory', ['constant',
    'RestUtilityFactory', 'ModelFactory', 'UtilErrorsFactory',
    '$rootScope', '$q',
    function EmiaRestUtilityFactory(constant, RestUtilityFactory,
                                    ModelFactory, UtilErrorsFactory, $rootScope, $q) {

        const restUtilService: any = {};

        restUtilService.GetDataBaseInfo = GetDataBaseInfo;

        restUtilService.PostCategory = PostCategory;
        restUtilService.GetCategories = GetCategories;
        restUtilService.DeleteCategory = DeleteCategory;

        restUtilService.PostQueCatAssoc = PostQueCatAssoc;
        restUtilService.GetQueCatAssoc = GetQueCatAssoc;

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

        return restUtilService;

        function GetAllowedCategories() {
            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/anaTipCat';

            return RestUtilityFactory.DeferredPromiseGet(path);
        }

        function GetDataBaseInfo() {

            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/dbInfo';

            return RestUtilityFactory.DeferredPromiseGet(path);

        }

        function GetCategories() {

            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/category';

            return RestUtilityFactory.DeferredPromiseGet(path);

        }

        function PostQueCatAssoc(queryToAdd) {
            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/queCatAssoc/post';

            return RestUtilityFactory.DeferredPromisePost(path, queryToAdd);
        }

        function GetQueCatAssoc() {

            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/queCatAssoc';

            return RestUtilityFactory.DeferredPromiseGet(path);

        }

        function PostCategory(parent, newCateg) {

            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/category';

            return RestUtilityFactory.DeferredPromisePost(path, ModelFactory
                .GetTemi14(parent, newCateg), null);
        }

        function DeleteCategory(category) {

            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/category/delete';

            return RestUtilityFactory.DeferredPromisePost(path, category);
        }

        function DeleteQuery(que, insQue) {
            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/query/delete';

            return RestUtilityFactory.DeferredPromisePost(path, {que: que, insQue: insQue});

        }

        function PostQuery(ttps15) {

            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/query';

            return RestUtilityFactory.DeferredPromisePost(path, ttps15);
        }

        function GetQueriesByTipCateg(tipCat, queries) {
            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/query/tipCateg';

            return RestUtilityFactory.DeferredPromisePost(path, queries, {
                tipCat: tipCat,
            });
        }

        function GetAlreadyAssociatedQuery(ccat, insCat, tipCat) {
            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/query/associatedQuery';

            return RestUtilityFactory.DeferredPromiseGet(path, {
                cat: ccat,
                tipCat: tipCat,
                insCat: insCat
            });
        }

        function GetRoutines() {
            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/routine';

            return RestUtilityFactory.DeferredPromiseGet(path, {});
        }

        function PostRoutine(routine) {
            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/routine';

            return RestUtilityFactory.DeferredPromisePost(path, routine);
        }

        function DeleteRoutine(routine) {
            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/routine/delete';

            return RestUtilityFactory.DeferredPromisePost(path, routine);
        }

        function PostRoutineQuery(rou, insRou, que, insQue) {
            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/routQuery';

            const data = ModelFactory.GetTemi18Pk(rou, insRou, que, insQue);

            return RestUtilityFactory.DeferredPromisePost(path, data);
        }

        function DeleteRoutineQuery(rou, insRou, que, insQue) {
            const path = constant.contextRoot + constant.restBasicPath
                + '/emia/routQuery/delete';

            return RestUtilityFactory.DeferredPromisePost(path, {
                rou: rou,
                insRou: insRou,
                que: que,
                insQue: insQue
            });
        }

    }]);