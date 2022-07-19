package it.cambi.qrgui.services

import com.fasterxml.jackson.databind.ObjectMapper
import it.cambi.qrgui.dao.generic.impl.FirstGenericDao
import it.cambi.qrgui.model.Temi15UteQue
import it.cambi.qrgui.query.model.QueryToJson
import spock.lang.Specification

class QueryServiceTest extends Specification {

    def objectMapper = Mock(ObjectMapper)
    def queryService = new QueryService(objectMapper)
    def temi15UteQue = Mock(Temi15UteQue)
    def firstGenericDao = Mock(FirstGenericDao)

    def "fail validation when statement is null"() {
        given:
        objectMapper.readValue(temi15UteQue.getJson(), QueryToJson.class) >> new QueryToJson()

        when:
        def wrapperResponse = queryService.checkQuery(temi15UteQue, false, firstGenericDao)

        then:
        !wrapperResponse.isSuccess()
    }

}
