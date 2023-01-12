package it.cambi.qrgui.services

import com.fasterxml.jackson.databind.ObjectMapper
import it.cambi.qrgui.api.model.UteQueDto
import it.cambi.qrgui.query.model.QueryToJson
import it.cambi.qrgui.services.taskExecutor.QueryService
import spock.lang.Specification

class QueryServiceTest extends Specification {

    def objectMapper = Mock(ObjectMapper)
    def queryService = new QueryService(objectMapper)
    def temi15UteQue = Mock(UteQueDto)

    def "fail validation when statement is null"() {
        given:
        objectMapper.readValue(temi15UteQue.getJson(), QueryToJson.class) >> new QueryToJson()

        when:
        def wrapperResponse = queryService.checkQuery(temi15UteQue, false, null)

        then:
        !wrapperResponse.isSuccess()
    }

    def "fail validation when statement contains forbidden statement"() {
        given:
        objectMapper.readValue(temi15UteQue.getJson(), QueryToJson.class) >> queryToJson

        when:
        def wrapperResponse = queryService.checkQuery(temi15UteQue, false, null)

        then:
        wrapperResponse.isSuccess() == result

        where:
        queryToJson                                                        | result
        QueryToJson.builder().statement("insert into table ...").build()   | false
        QueryToJson.builder().statement("delete from table").build()       | false
        QueryToJson.builder().statement("update table set ...").build()    | false
        QueryToJson.builder().statement("create table ...").build()        | false
        QueryToJson.builder().statement("drop table ...").build()          | false
        QueryToJson.builder().statement("select * from table ...").build() | false
    }
}
