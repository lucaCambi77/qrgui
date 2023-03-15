/**
 * 
 */
 import angular from 'angular';

angular.module('qrGuiApp').constant('constant', (function() {
	var whereCondition = {
		data : {
			name : {
				element : '- WHERE'
			}
		},
		isParameterSaved : false,
		operator : null,
		value : null,
		valueStart : null,
		valueEnd : null,
		constraints : {
			maxIntervalDateDays : null,
			maxIntervalDateTime : null,
			maxIntervalNumber : null,
			maxInSize : null,
		}
	}

	return {
		'TAM_LOGIN' : "https://intranettst.gruppo.autostrade.it/pkmslogout",

		'DES_MAX_LENGTH' : 80,
		'CURRENT_QUERY_VERSION' : 1,
		'contextRoot' : '/qrgui/',
		'restBasicPath' : 'rest',
		'GMT_POPUP_FORMAT' : 'yyyy-MM-dd HH:mm:ss',

		// local or Gmt time
		'GMT' : 'gmt',
		'LOCAL' : 'local',

		// Host
		'LOCALHOST' : 'localhost',
		// operators for query
		'COMPARISON' : {
			GT : '>', // greater than
			GE : '>=', // greater equal
			LT : '<', // less than
			LE : '<=', // less equal
			EQ : '=',
			IN : 'IN',
			BETWEEN : 'BETWEEN',
			LIKE : 'LIKE',
			ISNULL : 'IS NULL',
			ISNOTNULL : 'IS NOT NULL'
		// equal (also working for is null , must use NULL
		},
		'JUNCTION' : {
			AND : 'AND',
			OR : 'OR'
		// NOT : 'NOT'
		},
		'ORDERBY' : {
			ASC : 'ASC',
			DESC : 'DESC'
		// NOT : 'NOT'
		},
		'WHERE_CONDITION' : whereCondition,
		'SELECT' : {
			cque : null,
			id : "select",
			temi13DtbInf : null,
			version : null,
			tNam : null,
			lists : {
				select : [ {
					name : '- SELECT',
					type : null
				} ],
				where : {
					condition : [ whereCondition ]
				},
				join : [ '- JOIN' ],
				groupBy : [ '- GROUP BY' ],
				orderBy : [ {
					name : '- ORDER BY',
					orderType : 'ASC'
				} ]
			},
			rootSelection : null,
			categories : [],
			statement : null,
			json : null,
			junction : 'AND',
			onChange : false,
			isVisible : true,
			isQueryDataLoading : false,
			distinct : false
		},
		// as
		// parameter)
		'NE' : 'NE~', // not equal (also working for is not null, must use
		// null as
		// parameter)
		'IN' : 'IN~', // in

		// null codification
		'NULL' : 'null',

		// Content Type
		'JSONCONTENTTYPE' : {
			headers : {
				'Content-Type' : 'application/json;',
				'Cache-Control' : 'no-cache'
			}
		},

		// Type
		'ASC' : 'ASC',
		'DESC' : 'DESC',

		// Boolean
		'TRUE' : 'true',
		'FALSE' : 'false',

		// Return code di servizi rest
		'UNAUTHORIZED' : '401',

		// Default pageLength
		'PAGELENGTH' : 15,

		'NEW_CATEGORY' : {
			par : null,
			root : {
				id : {
					cat : null,
					insCat : null
				},
				temi20AnaTipCat : {
					tipCat : null
				}
			},
			des : null
		},

		'NEW_ROUTINE' : {
			rou : null,
			insRou : null,
			des : null,
			temi18RouQues : []
		},

		'FIELD_TYPES' : [ 'NUMBER', 'STRING', 'DATE', 'DATE_TRUNC' ],
		'CONSTRAINT_TYPES' : [ {
			id : 'TEMPORAL_INTERVAL',
			type : 'Intervallo Temporale'

		}, {
			id : 'NUMERIC_INTERVAL',
			type : 'Intervallo Numerico'

		}, {
			id : 'IN_SIZE',
			type : 'Dimensione IN'

		} ]

	}
})());