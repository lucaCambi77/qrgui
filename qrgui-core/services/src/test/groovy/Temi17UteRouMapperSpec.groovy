import it.cambi.qrgui.dto.Temi17UteRouDto
import it.cambi.qrgui.mapper.Temi17UteRouMapper
import it.cambi.qrgui.model.Temi17UteRou
import it.cambi.qrgui.model.Temi18RouQue
import it.cambi.qrgui.model.Temi18RouQueId
import org.mapstruct.factory.Mappers
import spock.lang.Specification

class Temi17UteRouMapperSpec extends Specification {

    def mapper = Mappers.getMapper(Temi17UteRouMapper)

    def "should map Temi17UteRou entity to DTO"() {
        given:
        def entity = new Temi17UteRou()
        entity.setRou(123L)
        entity.setInsRou(new Date(1699999999999L))
        entity.setDes("Test Description")

        def temi18 = new Temi18RouQue();
        def id = new Temi18RouQueId();
        id.rou = 123L
        id.insRou = new Date(1699999999999L)
        id.insQue = new Date(1699999999999L)
        id.que = 1

        temi18.setId(id);

        entity.setTemi18RouQues(Set.of(temi18))

        when:
        def dto = mapper.toDto(entity)

        then:
        dto != null
        dto.rou == 123L
        dto.insRou == new Date(1699999999999L)
        dto.des == "Test Description"
        dto.temi18RouQues != null
        dto.temi18RouQues.size() == 1

        and:
        def t18 = dto.temi18RouQues.iterator().next()
        t18.id.que == 1
        t18.id.rou == 123L
        t18.id.insQue == new Date(1699999999999L)
        t18.id.insRou == new Date(1699999999999L)
    }

    def "should map Temi17UteRouDTO to entity"() {
        given:
        def dto = new Temi17UteRouDto()

        dto.rou = 456L
        dto.insRou = new Date(1688888888888L)
        dto.des = "Mapped Back"

        when:
        def entity = mapper.toEntity(dto)

        then:
        entity != null
        entity.rou == 456L
        entity.insRou == new Date(1688888888888L)
        entity.des == "Mapped Back"
    }
}
