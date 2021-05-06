'use strict';

/**
 * @ngdoc overview
 * @name qrGuiApp
 * @description # qrGuiApp
 * 
 * Main module of the application.
 */
import angular from 'angular';

angular
		.module('qrGuiApp')
		.directive(
				'routineCategories', 
				function($compile, $rootScope) {

					return {
						restrict : 'A',
						link : function(scope, element, attrs) {

							/**
							 * Se sono admin, allora devo togliere la categoria
							 * aggiungi
							 */
							var isAdmin = null != $rootScope.ertaQrGuiUser
									&& $rootScope.ertaQrGuiUser.admin;
							var categoryLength = (null == $rootScope.categories) ? 0
									: $rootScope.categories.length;

							var length = isAdmin ? (categoryLength - 1)
									: categoryLength;

							var subCateg = '<div ng-repeat="item in cat.childrens">'
									+ '	      <div style="margin : 1em;" class="panel panel-default">'
									+ '			<div class="panel-heading"> <h4 class="panel-title">'
									+ '     	 <a data-toggle="collapse" data-parent="#accordion" data-target="#collapse-{{item.root.id.cat}}"'
									+ ' 			href><i class="fa fa-book" aria-hidden="true"></i>'
									+ '        		</span><b style="color : {{colorYellow}}"> {{item.root.temi20AnaTipCat.des}}</b> <br> <b> {{item.root.des}} </b></a>'
									+ '   			 </h4>'
									+ '		 	</div>'
									+ '		  <div style="height: 10em; overflow-y: scroll; margin: 0.5em">'
									+ '			<ul>'
									+ '				<li ui-draggable="true" drag="query" style="margin: 0.3em;" ng-repeat="query in item.queries">'
									+ '						<div style="color : {{colorBlu}}"class="col-sm-12">{{query.nam}}</div>'
									+ '				</li>'
									+ '			</ul>'
									+ '		     <div ng-switch on="item.childrens.length > 0">'
									+ '		        <div ng-switch-when="true">'
									+ '		          <div ng-init="childrens = item.childrens;" ng-include="\'views/partial/subCategoriesRouPartial.html\'"></div>'
									+ '				</div>'
									+ ' 		</div>'
									+ ' 	  </div>'
									+ '      </div>	</div> ';

							var templ = '<div ng-repeat="cat in categories | limitTo:'
									+ length.toString()
									+ '"> <div style="margin : 1em;" class="panel panel-default">'
									+ '  <div class="panel-heading">'
									+ '    <h4 class="panel-title">'
									+ '      <a data-toggle="collapse" data-parent="#accordion" data-target="#collapse-{{cat.root.id.cat}}"'
									+ ' 	href><i class="fa fa-book" aria-hidden="true"></i>'
									+ '        </span><b style="color : {{colorYellow}}"> {{cat.root.temi20AnaTipCat.des}} </b> <br> <b> {{cat.root.des}} </b></a>'
									+ '    </h4>'
									+ '  </div>'
									+ '  <div style="height:10em; overflow-y:scroll; margin: 0.5em" >'
									+ '			<ul>'
									+ '				<li ui-draggable="true" drag="query" style="margin: 0.3em;" ng-repeat="query in cat.queries">'
									+ '						<div style="color : {{colorBlu}}"class="col-sm-12">{{query.nam}}</div>'
									+ '				</li>'
									+ '			</ul>'
									+ subCateg
									+ '  </div>' + ' </div>' + '</div>';

							element.append($compile(templ)(scope));
						},
						controller : function($scope) {

						},
						template : function(element, attrs) {

							return '<div style="margin : 1em">'
									+ '<h3>Categorie</h3>' + '</div>';
						}
					};
				})
		.directive(
				'inputqueryDateBetween',
				function() {

					return {
						restrict : 'AE',
						template : function(element, attrs) {

							var template = "";

							template += '<div ng-if="!$first && (item.data.type.indexOf(\'TIME\') >= 0 || item.data.type.indexOf(\'DATE\') >= 0) && (item.data.operator == \'BETWEEN\' && item.data.operator != null)"><b>start</b> <input'
									+ '			style="width: 12em" datetime-picker="{{item.data.datePopup.format}}" datepicker-Timezone'
									+ '			type="text" ng-model="item.data.valueStart"'
									+ '			is-open="item.data.datePopup.isOpen"'
									+ '			datepicker-options="item.data.datePopup.dateOptions"'
									+ '			timepicker-options="item.data.datePopup.timePickerOptions"'
									+ '			alt-input-formats="item.data.datePopup.altInputFormats"'
									+ '			style="text-align: center" show-button-bar="false" /> <span>'
									+ '				<button type="button" class="btn btn-default"'
									+ '					ng-click="item.data.datePopup.openDatePicker($event, item.data.datePopup.isOpen)">'
									+ '					<i class="glyphicon glyphicon-calendar"></i> &nbsp'
									+ '				</button>'
									+ '					</span>'
									+ '		<b>end </b><input'
									+ '			style="width: 12em" datetime-picker="{{item.data.datePopup1.format}}" datepicker-Timezone'
									+ '			type="text" ng-model="item.data.valueEnd"'
									+ '			is-open="item.data.datePopup1.isOpen"'
									+ '			datepicker-options="item.data.datePopup1.dateOptions"'
									+ '			timepicker-options="item.data.datePopup1.timePickerOptions"'
									+ '			alt-input-formats="item.data.datePopup1.altInputFormats"'
									+ '			style="text-align: center" show-button-bar="false" /> <span>'
									+ '				<button type="button" class="btn btn-default"'
									+ '					ng-click="item.data.datePopup1.openDatePicker($event, item.data.datePopup1.isOpen)">'
									+ '					<i class="glyphicon glyphicon-calendar"></i> &nbsp'
									+ '				</button>'
									+ '					</span>'
									+ ' 		</div>'

							return template;
						}
					};
				})
		.directive(
				'datepickerTimezone',
				function($rootScope, constant) {
					return {
						restrict : 'A',
						priority : 1,
						require : 'ngModel',
						link : function(scope, element, attrs, ctrl) {
							ctrl.$formatters.push(function(value) {

								if (value == null) {
									return value; // needed for
									// null/empty/undefined
									// values
								}

								var dt = new Date(value);
								if ($rootScope.dateType == constant.GMT) {

									// 'undo' the timezone offset again (so we
									// end up on the original date again)
									dt.setMinutes(dt.getMinutes()
											+ dt.getTimezoneOffset());

								}

								return dt;
							});

							ctrl.$parsers
									.push(function(value) {

										// var date = new Date(value.getTime()
										// - (60000 *
										// value.getTimezoneOffset()));

										if (value == null) {
											return value; // needed for
											// null/empty/undefined
											// values
										}

										if ($rootScope.dateType == constant.GMT) {

											// 'undo' the timezone offset again
											// (so we
											// end up on the original date
											// again)
											value
													.setMinutes(value
															.getMinutes()
															+ (-1 * value
																	.getTimezoneOffset()));

										}

										if (Object.prototype.toString
												.call(value) === '[object Date]')
											return value.getTime();

										return value;
									});
						}
					};
				})
		.directive(
				'onReadFile',
				function($parse) {
					return {
						restrict : 'A',
						scope : false,
						link : function(scope, element, attrs) {
							var fn = $parse(attrs.onReadFile);

							element
									.on(
											'change',
											function(onChangeEvent) {
												var reader = new FileReader();

												reader.onload = function(
														onLoadEvent) {
													scope
															.$apply(function() {
																fn(
																		scope,
																		{
																			$fileContent : onLoadEvent.target.result
																		});
															});
												};

											// 	reader
											// 			.readAsBinaryString((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
											// 
										});
						}
					};
				})
