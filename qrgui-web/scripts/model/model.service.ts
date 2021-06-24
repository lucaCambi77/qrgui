/**
 * 
 * 
 */
 import angular from 'angular';

(function() {
	'use strict';

	angular.module('qrGuiApp').factory('ModelFactory', ModelFactory);

	function ModelFactory() {

		var modelService : any =  {};

		modelService.GetTemi14 = GetTemi14;
		modelService.GetTemi15 = GetTemi15;
		modelService.GetTemi18Pk = GetTemi18Pk;
		modelService.GetHtmlQueryFromTemi15 = GetHtmlQueryFromTemi15;

		modelService.GetParameter = GetParameter;
		modelService.GetQueryAttribute = GetQueryAttribute;
		modelService.GetConstraint = GetConstraint;
		modelService.GetQueryRest = GetQueryRest;

		return modelService;

		function GetParameter() {
			this.name;
			this.type;
			this.def;
		}

		function GetQueryAttribute() {
			this.attrName;
			this.alias;
			this.parameter;
			this.operator;
		}

		function GetConstraint() {
			this.attrName;
			this.parameters;
			this.message;
			this.constrType;
			this.maxIntervalDays = 0;
			this.maxIntervalHours = 0;
			this.maxIntervalMin = 0;
			this.maxIntervalSec = 0;
			this.maxIntervalNumber = 0;
			this.maxInSize = 0;
		}

		function GetTemi18Pk(rou, insRou, que, insQue) {

			return {
				rou : rou,
				insRou : insRou,
				que : que,
				insQue : insQue
			};

		}

		function GetTemi14(parent, newCateg) {

			var temi14 = {
				id : {

					cat : newCateg.root.id.cat,
					insCat : newCateg.root.id.insCat
				},

				par : parent == null ? null : parent.root.id.cat,
				insPar : parent == null ? null : parent.root.id.insCat,
				des : newCateg.root.des,

				temi20AnaTipCat : {
					tipCat : newCateg.root.temi20AnaTipCat.tipCat,
					des : null,
					temi14UteCats : null
				}
			};

			return temi14;
		}

		function GetTemi15(query, categ, queryRest) {

			var listTemi16 = [];

			listTemi16.push({
				id : {
					que : null == query.que ? null : query.que,
					insQue : null == query.insQue ? null : query.insQue,
					cat : categ.cat,
					insCat : categ.insCat
				},
				temi15UteQue : null
			})

			return {
				id : {
					que : null == query.que ? null : query.que,
					insQue : null == query.insQue ? null : query.insQue
				},
				json : JSON.stringify(queryRest, replacer),
				nam : query.nam,
				temi13DtbInf : {
					id : {
						typ : query.temi13DtbInf == null ? null
								: query.temi13DtbInf.id.typ,
						sch : query.temi13DtbInf == null ? null
								: query.temi13DtbInf.id.sch
					},
					temi15UteQues : null
				},
				temi16QueCatAsses : listTemi16,
				temi18RouQues : null
			}
		}

		function GetQueryRest() {

		}

		function GetHtmlQueryFromTemi15(temi15) {

			return JSON.parse(temi15.json);

			// return {
			//
			// version : tjson.version,
			// name : tjson.name,
			// statement : tjson.statement,
			//
			// };
		}

		/**
		 * Escludo alcuni campi nella serializzazione del json che andrà nel
		 * database, tra cui le $$hashKey, il paginatore ed il json che è il
		 * campo stesso in cui andrà a finire questa serializzazione, quindi non
		 * lo serializzo altrimenti avrei il json dentro al json
		 */
		function replacer(key, value) {
			// if (key == "cque")
			// return undefined;
			// else
			if (key == "$$hashKey")
				return undefined;
			else if (key == "pager")
				return undefined;
			else if (key == "json")
				return undefined;
			else if (key == "isDeleted")
				return undefined;
			else if (key == "datePopup")
				return undefined;
			else
				return value;
		}
	}

})();