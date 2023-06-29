package test

import com.fasterxml.jackson.databind.ObjectMapper
import it.cambi.qrgui.api.model.UteQueDto
import it.cambi.qrgui.query.model.QueryToJson
import it.cambi.qrgui.taskExecutor.QueryService
import spock.lang.Specification

class QueryServiceTest extends Specification {

    def queryService = new QueryService()
    def uteQue = GroovyMock(UteQueDto)

    def "fail validation when statement is null"() {

        when:
        def wrapperResponse = queryService.checkQuery(new QueryToJson(), Optional.empty())

        then:
        !wrapperResponse.isSuccess()
    }

    def "fail validation when statement is forbidden"() {
        given:
        uteQue.json() >> queryToJson

        expect:
        queryService.checkQuery(queryToJson, Optional.empty()).isSuccess() == result

        where:
        queryToJson                                                        | result
        QueryToJson.builder().statement("insert into table ...").build()   | false
        QueryToJson.builder().statement("delete from table").build()       | false
        QueryToJson.builder().statement("update table set ...").build()    | false
        QueryToJson.builder().statement("create table ...").build()        | false
        QueryToJson.builder().statement("drop table ...").build()          | false
        QueryToJson.builder().statement("select * from table ...").build() | false
    }

    def "fail validation when statement is forbidden"() {

        expect:
        maxArea(height) == result

        where:
        height | result
        new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7} 49


    }

    int maxArea(int[] height) {
        int l = height[0]

        for (int i = 1; i < height.length; i++) {

        }
    }
}
