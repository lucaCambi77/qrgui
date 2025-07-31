package it.cambi.qrgui.mapper;

import it.cambi.qrgui.dto.Temi18RouQueIdDto;
import it.cambi.qrgui.model.Temi18RouQueId;
import org.mapstruct.Mapper;

// For Temi18RouQueId
@Mapper(componentModel = "spring")
public interface Temi18RouQueIdMapper {
    Temi18RouQueIdDto toDto(Temi18RouQueId id);
    Temi18RouQueId toEntity(Temi18RouQueIdDto dto);
}
