package test

import com.fasterxml.jackson.databind.ObjectMapper
import it.cambi.qrgui.api.model.UteQueDto
import it.cambi.qrgui.query.model.QueryToJson
import it.cambi.qrgui.taskExecutor.QueryService
import spock.lang.Specification

class QueryServiceTest extends Specification {

    def objectMapper = Mock(ObjectMapper)
    def queryService = new QueryService(objectMapper)
    def uteQue = GroovyMock(UteQueDto)

    def "fail validation when statement is null"() {
        given:
        objectMapper.readValue(uteQue.json(), QueryToJson.class) >> new QueryToJson()

        when:
        def wrapperResponse = queryService.checkQuery(uteQue, Optional.empty())

        then:
        !wrapperResponse.isSuccess()
    }

    def "fail validation when statement is forbidden"() {
        given:
        objectMapper.readValue(uteQue.json(), QueryToJson.class) >> queryToJson

        expect:
        queryService.checkQuery(uteQue, Optional.empty()).isSuccess() == result

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
