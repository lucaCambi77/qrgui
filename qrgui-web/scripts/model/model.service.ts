/**
 *
 *
 */
import angular from 'angular';

(function () {
    'use strict';

    angular.module('qrGuiApp').factory('ModelFactory', ModelFactory);

    function ModelFactory() {

        var modelService: any = {};

        modelService.GetTemi14 = GetTemi14;
        modelService.GetTemi15 = GetTemi15;
        modelService.GetTemi18Pk = GetTemi18Pk;
        modelService.GetHtmlQueryFromTemi15 = GetHtmlQueryFromTemi15;

        modelService.GetParameter = GetParameter;
        modelService.GetQueryAttribute = GetQueryAttribute;
        modelService.GetConstraint = GetConstraint;

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
                rou: rou,
                insRou: insRou,
                que: que,
                insQue: insQue
            };

        }

        function GetTemi14(parent, newCateg) {

            return {

                cat: newCateg.root.cat,
                insCat: newCateg.root.insCat,

                par: parent == null ? null : parent.root.cat,
                insPar: parent == null ? null : parent.root.insCat,
                des: newCateg.root.des,

                temi20AnaTipCat: {
                    tipCat: newCateg.root.temi20AnaTipCat.tipCat,
                    des: null,
                    temi14UteCats: null
                }
            };
        }

        function GetTemi15(query, categ, queryRest) {

            const listTemi16 = [];

            listTemi16.push({
                id: {
                    que: null == query.que ? null : query.que,
                    insQue: null == query.insQue ? null : query.insQue,
                    cat: categ.cat,
                    insCat: categ.insCat
                },
                temi15UteQue: null
            });

            return {
                que: null == query.que ? null : query.que,
                insQue: null == query.insQue ? null : query.insQue,
                json: JSON.stringify(queryRest, replacer),
                nam: query.nam,
                tenant: query.tenant,
                temi16QueCatAsses: listTemi16,
                temi18RouQues: null
            };
        }

        function GetHtmlQueryFromTemi15(temi15) {

            return JSON.parse(temi15.json);

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