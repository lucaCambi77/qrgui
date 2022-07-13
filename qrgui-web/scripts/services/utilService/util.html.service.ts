/**
 * !!Importante Function per la creazione dinamica di html. In questo servizio
 * risiede quasi tutta la logica della parte html dell'applicazione.
 * 
 */
import angular from 'angular';

angular.module('qrGuiApp').factory('HtmlUtilityFactory',['$compile', '$rootScope', 'DateUtilityFactory', 'constant',

	function HtmlUtilityFactory($compile, $rootScope, DateUtilityFactory, constant) {

		var htmlUtilService: any = {};
		htmlUtilService.GetQueryListTemplate = GetQueryListTemplate;
		htmlUtilService.GetQueryErrorListTemplate = GetQueryErrorListTemplate;
		htmlUtilService.SetCategoriesListTemplate = SetCategoriesListTemplate;
		htmlUtilService.SetRoutineListTemplate = SetRoutineListTemplate;
		htmlUtilService.GetQueryTableTemplate = GetQueryTableTemplate;
		htmlUtilService.GetQueryPaginationTemplate = GetQueryPaginationTemplate;

		return htmlUtilService;

		function SetRoutineListTemplate(scope, routines) {

			/* numero di oggetti per riga */
			var factor = (null != $rootScope.ertaQrGuiUser && $rootScope.ertaQrGuiUser.admin) ? 3
				: 4;

			/*
			 * Numeri di elementi per riga basato sulla struttura a griglia di
			 * bootstrap x12
			 */
			var rowDim = 12 / factor;

			var routineLenght = routines.length;

			var parts = routineLenght / factor, rows, start = 0, templateRoutine = "";

			rows = Math.floor(parts);

			/*
			 * Creo il template limitando gli oggetti per riga per il factor e
			 * con partenza dinamica rispetto allo scorrimentod dell'array delle
			 * routines
			 */
			for (var i = 0; i < (rows + 1); i++) {
				templateRoutine += '<div class="row">'
					+ '<div ng-repeat="rout in routines | limitTo:'
					+ factor.toString()
					+ ':'
					+ start.toString()
					+ '">'
					+ '<div class="col-sm-'
					+ rowDim.toString()
					+ '">'
					+ '<div class="panel panel-default" id="routine'
					+ i
					+ '">'
					+ '	  <div class="panel-heading" ui-on-Drop="routine.onQueryDrop(rout, $data, $index)">'
					/* Button new category */
					+ '		<div ng-if="routine.checkLast($index, '
					+ (i + 1)
					+ ', '
					+ factor
					+ ')"> <div class="row"> <div class="col-sm-10"> <input maxlength="{{desMaxLength}}" class="form-control" ng-model="rout.des" type="text" placeholder="... nome nuova routine"/> </div>'
					+ '			<div class="col-sm-2"> <button title="{{routine.newRoutineTitle}}" class="btn btn-sm" ng-click="routine.newRoutine(rout)">'
					+ '				<i class="fa fa-plus" aria-hidden="true"></i>'
					+ '				</button>'
					+ '			</div> </div>'
					+ '		</div>'
					/*
					 * Non è l'ultimo elemento, aggiungo buttons per poter
					 * cancellare e poter eseguire
					 */
					+ '		<div ng-if="!routine.checkLast($index, '
					+ (i + 1)
					+ ', '
					+ factor
					+ ')"> <div class="row"> <div class="col-sm-8"> {{rout.des}} </div> <div class="col-sm-1" style="margin-right: 0.5em;"> <button title="{{routine.deleteRoutineTitle}}" ng-if="ertaQrGuiUser.admin" class="btn btn-sm" ng-click="routine.deleteRoutine(rout,'
					+ i
					+ ')"'
					+ '> <i class="fa fa-trash-o" aria-hidden="true"></i>'
					+ '</button> </div>'
					/* Button execute routine */
					+ ' 			 <div class="col-sm-1"> <button title="{{routine.executeRoutineTitle}}" class="btn btn-sm" ng-click="routine.executeRoutine(rout)"'
					+ ' 			> <span class="glyphicon glyphicon-play" aria-hidden="true"></span> </button>'
					+ '			</div> </div> '
					+ '		</div>'
					+ '  </div>'
					+ ' <div ng-if="!routine.checkLast($index, '
					+ (i + 1)
					+ ', '
					+ factor
					+ ')" style="height:15em; overflow-y:scroll;">'
					+ '	<ul>'
					+ '		<li style="margin: 0.3em; list-style-type:none;" ng-repeat="query in rout.temi18RouQues | orderBy:\'temi15UteQue.nam\'">'
					+ '       <div class="row">'
					+ '				<div style="color : {{colorBlu}}"class="col-sm-9">{{query.temi15UteQue.nam}}</div>'
					+ ' 				<div class="col-sm-3"> <button title="{{routine.deleteQueryRoutineTitle}}" ng-if="ertaQrGuiUser.admin" class="btn btn-sm" ng-click="routine.deleteQueryRoutine(query, rout)"> <i class="fa fa-trash-o" aria-hidden="true"></i>'
					+ '							</button>'
					+ '					</div>'
					+ '	     </div>'
					+ '		</li>'
					+ '</ul> </div> </div> </div> </div> </div>';

				start = start + factor;
			}

			$("#routineList").html($compile(templateRoutine)(scope));

		}

		function SetCategoriesListTemplate(scope, categories) {

			if ((!categories || (categories && categories.length == 0)) && $rootScope.ertaQrGuiUser.admin) {
				$rootScope.categories = [];
				$rootScope.categories.push(angular.copy(constant.NEW_CATEGORY));
			}
			/* numero di oggetti per riga */
			var factor = 3;

			/*
			 * Numeri di elementi per riga basato sulla struttura a griglia di
			 * bootstrap x12
			 */
			var rowDim = 12 / factor;

			var categoryLenght = categories.length;

			var parts = categoryLenght / factor, rows, start = 0, template = "";

			rows = Math.floor(parts) + 1;

			/*
			 * Creo il template limitando gli oggetti per riga per il factor e
			 * con partenza dinamica rispetto allo scorrimento dell'array delle
			 * categorie
			 */
			for (var i = 0; i < rows; i++) {
				template += '<div class="row">'
					+ '		<div ng-repeat="categ in categories | limitTo:'
					+ factor.toString()
					+ ':'
					+ start.toString()
					+ '">'
					+ '				<div id="categ-{{categ.root.cat}}" class="col-sm-'
					+ rowDim.toString()
					+ '				">'
					+ '				<div class="panel panel-default">'
					+ '					<div class="panel-heading" ui-on-Drop="category.createSubCategory(categ, $data)" >'
					/* Ultima categoria, aggiungi */
					+ '						<div ng-if="category.CheckLast($index, '
					+ (i + 1)
					+ ', '
					+ factor
					+ ')"> 					<div class="row"> <div class="col-sm-10"> <input maxlength="{{desMaxLength}}" class="form-control" ng-model="categ.root.des" type="text" placeholder="... nome nuova categoria"/> </div>'
					+ '							<div class="col-sm-2"> <button title ="{{category.addCategoryTitle}}" class="btn btn-sm" ng-click="category.newCategory(null, categ)">'
					+ '								<i class="fa fa-plus" aria-hidden="true"></i>'
					+ '								</button>'
					+ '							</div>'
					+ '						</div>'
					+ '						</div>'
					/* Categoria su db */
					+ '						<div ng-if="!category.CheckLast($index, ' + (i + 1) + ', ' 	+ factor + ')"> '
					+ ' <div class="row"> <div class="col-sm-8" ui-draggable="true" drag="categ"> <h3 class="panel-title">'
					+ ' <a data-toggle="collapse" data-parent="#accordion" data-target="#collapse-{{categ.root.cat}}"'
					+ ' 		href><i style="margin-right: 0.3em" class="fa fa-book" aria-hidden="true"></i><b style="color : {{colorYellow}}">{{categ.root.des}}</b> <br> <b> {{categ.root.temi20AnaTipCat.des}} </b> </a></h3></div>'
					+ '						<div class="col-sm-1"> <button title="{{category.addQueryTitle}}" ng-if="ertaQrGuiUser.admin" class="btn btn-sm" ng-click="category.addQuery(categ)"> <i class="fa fa-plus" aria-hidden="true"></i>'
					+ '								</button>'
					+ '						</div>'
					+ ' 					<div class="col-sm-1"> <button title="{{category.deleteCategoryTitle}}" ng-if="ertaQrGuiUser.admin" class="btn btn-sm" ng-click="category.deleteCategory(categ)"> <i class="fa fa-trash-o" aria-hidden="true"></i>'
					+ '								</button>'
					+ '						</div>'
					+ ' 					<div class="col-sm-1"> <button title="{{category.modifyCategoryTitle}}" ng-if="ertaQrGuiUser.admin" class="btn btn-sm" ng-click="category.modifyCategory(categ)"> <i class="fa fa-edit" aria-hidden="true"></i>'
					+ '								</button>'
					+ '						</div>'
					+ '  					</div>'
					+ '						</div>'
					+ ' 				</div>'
					/* Nuova categoria */
					+ '						<div style="margin : 0.5em" ng-if="category.CheckLast($index, '
					+ (i + 1)
					+ ', '
					+ factor
					+ ')"><div class="form-group row"> <label class="col-sm-5 control-label" style="color : {{colorRed}}">Tipo di Categoria</label>'
					+ '		<div class="col-sm-6"> <select ng-model="categ.root.temi20AnaTipCat" class="form-control" ng-options="option.des for option in tipCategories track by option.tipCat"></select>'
					+ '	  </div>'
					+ '  </div>'
					+ '  					</div>'
					/* Queries */
					+ '						<div style="height:15em; overflow-y:scroll; margin : 0.5em" ng-if="!category.CheckLast($index, '
					+ (i + 1)
					+ ', '
					+ factor
					+ ')">'
					+ ' 				<ul>'
					+ '						<li style="margin: 0.3em; list-style-type:none;" ng-repeat="query in categ.queries  | orderBy:\'nam\'" >'
					+ '					<div class="row">'
					+ '							<div class="col-sm-2"> <button title="{{category.executeQueryTitle}}" class="btn btn-sm" ng-click="category.executeQueryFromCat(query)">'
					+ '								<i class="fa fa-play-circle-o" aria-hidden="true"></i>'
					+ '								</button>'
					+ '							</div>'
					+ '							<div style="color : {{colorBlu}}"class="col-sm-7">{{query.nam}}</div>'
					/* Delete Query */
					+ '						<div class="col-sm-1"> <button title="{{category.deleteQueryTitle}}" ng-if="ertaQrGuiUser.admin" class="btn btn-sm" ng-click="category.deleteQuery(query, categ)" > <i class="fa fa-trash-o" aria-hidden="true"></i>'
					+ '								</button>'
					+ '						</div>'
					/* Update Query */
					+ '						<div class="col-sm-1"> <button title={{category.modifyQueryTitle}} ng-if="ertaQrGuiUser.admin" class="btn btn-sm" ng-click="category.updateCategoryQuery(query, categ)" > <i class="fa fa-edit" aria-hidden="true"></i>'
					+ '								</button>'
					+ '						</div>'
					+ '					</div>'
					+ '						 </li>' + '					</ul>' + GetSubCategories()
					/* Fine queries */
					+ ' </div> </div> </div> </div> </div>';

				start = start + factor;
			}

			$("#categories").html($compile(template)(scope));

		}

		function GetSubCategories() {

			return '<div ng-repeat="item in categ.childrens"> <div style="margin : 1em;" class="panel panel-default" id="categ-{{item.root.cat}}">'
				+ '<div class="panel-heading" ui-on-Drop="category.createSubCategory(item, $data)">'
				+ ' 	<div class="row"> <div class="col-sm-8" ui-draggable="true" drag="item"> <i class="fa fa-book" aria-hidden="true"></i> <b style="color : {{colorYellow}}">{{item.root.des}} </b> <br> <b> {{item.root.temi14AnaCat.des}}</b> '
				+ ' </div> 	<div class="col-sm-1"> <button title="{{category.addQueryTitle}}" ng-if="ertaQrGuiUser.admin" class="btn btn-sm" ng-click="category.addQuery(item)"> <i class="fa fa-plus" aria-hidden="true"></i>'
				+ '								</button>'
				+ '						</div>'
				+ ' 					<div class="col-sm-1"> <button title="{{category.deleteCategoryTitle}}" ng-if="ertaQrGuiUser.admin" class="btn btn-sm" ng-click="category.deleteCategory(item)"> <i class="fa fa-trash-o" aria-hidden="true"></i>'
				+ '								</button>'
				+ '						</div>'
				+ ' 					<div class="col-sm-1"> <button title="{{category.modifyCategoryTitle}}" ng-if="ertaQrGuiUser.admin" class="btn btn-sm" ng-click="category.modifyCategory(item)"> <i class="fa fa-edit" aria-hidden="true"></i>'
				+ '								</button>'
				+ '						</div>'
				+ '</div> </div>'
				+ '			<div>'
				/* Queries */
				+ '	<div style="height:10em; overflow-y:scroll; margin : 0.5em">'
				+ ' 				<ul>'
				+ '						<li style="margin: 0.3em; list-style-type:none;" ng-repeat="query in item.queries | orderBy:\'nam\'" >'
				+ '					<div class="row">'
				+ '							<div class="col-sm-2"> <button title="{{category.executeQueryTitle}}" class="btn btn-sm" ng-click="category.executeQueryFromCat(query)">'
				+ '								<i class="fa fa-play-circle-o" aria-hidden="true"></i>'
				+ '								</button>'
				+ '							</div>'
				+ '							<div style="color : {{colorBlu}}"class="col-sm-7">{{query.nam}}</div>'
				/* Delete Query */
				+ '							<div class="col-sm-1"> <button ng-if="ertaQrGuiUser.admin" title="{{category.deleteQueryTitle}}" class="btn btn-sm" ng-click="category.deleteQuery(query, item)" > <i class="fa fa-trash-o" aria-hidden="true"></i>'
				+ '								</button>'
				+ '							</div>'
				/* Update Query */
				+ '							<div class="col-sm-1"> <button title={{category.modifyQueryTitle}} ng-if="ertaQrGuiUser.admin" class="btn btn-sm" ng-click="category.updateCategoryQuery(query, categ)" > <i class="fa fa-edit" aria-hidden="true"></i>'
				+ '								</button>'
				+ '							</div>'
				+ '					</div>'
				+ '						 </li>'
				+ '					</ul>'
				+ '		     <div ng-switch on="item.childrens.length > 0">'
				+ '		        <div ng-switch-when="true">'
				+ '		          <div ng-init="childrens = item.childrens;" ng-include="\'views/partial/subCategoriesPartial.html\'">'
				+ ' 			 </div>'
				+ '				</div>'
				+ ' 		</div>'
				+ '	</div>'
				+ '			</div>' + '			</div>' + '      	</div> ';
		}

		/* Creazione dinamica della tabella dato il risultato della query */
		function GetQueryListTemplate(resultList, attrlist, queryPosition) {

			var template = GetQueryTableTemplate(resultList, attrlist);

			/* Paginatore */
			var pagination = GetQueryPaginationTemplate(queryPosition);

			return template.concat(pagination).concat('</div>');
		}

		function GetQueryTableTemplate(resultList, attrlist) {

			var template = '<div class="panel panel-default" style="margin: 1em;"> <div class="table-responsive"> <table class="table table-responsive table-bordered table-striped table-condensed table-bordered">';

			var firstRow = '<thead> <tr>';

			/* Creo l'header della tabella dai nomi delle multiselect */
			for (var i = 0; i < attrlist.length; i++) {

				firstRow += '<th>' + attrlist[i].as + '</th>';

				if (i == attrlist.length - 1)
					firstRow += '</tr></thead>';

			}

			var body = '<tbody>', row;

			/*
			 * Creo i valori delle celle a seconda del tipo. Se è una data la
			 * converto in formato data UTC
			 */
			for (var i = 0; i < resultList.length; i++) {
				var row: any = '<tr>';

				for (var j = 0; j < attrlist.length; j++) {

					var htmlCellValue = resultList[i][j];

					if (attrlist[j].type == 'DATE')
						htmlCellValue = DateUtilityFactory
							.GetUtcDateStringFromLong(resultList[i][j],
								'/', true);

					if (attrlist[j].type == 'DATE_TRUNC')
						htmlCellValue = DateUtilityFactory
							.GetUtcDateStringFromLong(resultList[i][j],
								'/', false);

					row += '<td>' + htmlCellValue + '</td>';

					if (j == attrlist.length - 1)
						row += '</tr>';
				}

				body += row;
			}

			template += firstRow.concat(body + '</tbody>');
			template += '</table></div>';

			return template;
		}

		function GetQueryPaginationTemplate(queryPosition) {
			return '<div class="text-center">' + '<!-- pager -->'
				+ '<ul ng-show="query'
				+ queryPosition
				+ '.pager.pages.length > 0" class="pagination pagination-sm">'
				+ '	<li ng-class="{disabled:query'
				+ queryPosition
				+ '.pager.currentPage === 1}"><a href'
				+ '		ng-click="submitQuery(1, '
				+ queryPosition
				+ ')">{{ \'PAGINATION.FIRST\' | translate }}</a></li>'
				+ '	<li ng-class="{disabled:query'
				+ queryPosition
				+ '.pager.currentPage === 1}"><a href'
				+ '		ng-click="submitQuery(query'
				+ queryPosition
				+ '.pager.currentPage - 1, '
				+ queryPosition
				+ ')">{{'
				+ '			\'PAGINATION.PREVIOUS\' | translate }}</a></li>'
				+ '	<li ng-repeat="page in query'
				+ queryPosition
				+ '.pager.pages"'
				+ '		ng-class="{active: query'
				+ queryPosition
				+ '.pager.currentPage === page}"><a href'
				+ '		ng-click="submitQuery(page, '
				+ queryPosition
				+ ')">{{page}}</a></li>'
				+ '	<li ng-class="{disabled:query'
				+ queryPosition
				+ '.pager.currentPage === query'
				+ queryPosition
				+ '.pager.totalPages}"><a'
				+ '		href ng-click="submitQuery(query'
				+ queryPosition
				+ '.pager.currentPage + 1,'
				+ queryPosition
				+ ')">{{'
				+ '			\'PAGINATION.NEXT\' | translate }}</a></li>'
				+ '	<li ng-class="{disabled:query'
				+ queryPosition
				+ '.pager.currentPage === query'
				+ queryPosition
				+ '.pager.totalPages}">'
				+ '		<a href ng-click="submitQuery(query'
				+ queryPosition
				+ '.pager.totalPages, '
				+ queryPosition
				+ ')">{{'
				+ '			\'PAGINATION.LAST\' | translate }}</a>'
				+ '	</li>'
				+ '</ul> </div>';
		}

		function GetQueryErrorListTemplate(errors) {

			var errorUl = '<div class="panel panel-default" style="margin: 1em;"> <ul>';

			for (var error in errors) {
				errorUl += '<li style="margin: 0.3em; color:red;">'
					+ errors[error] + '</li>';

			}

			errorUl += '</ul> </div>'

			return errorUl;
		}

	}])