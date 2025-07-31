package it.cambi.qrgui.mapper;

import it.cambi.qrgui.dto.Temi15UteQueIdDto;
import it.cambi.qrgui.model.Temi15UteQueId;
import org.mapstruct.Mapper;

// For Temi15UteQueId
@Mapper(componentModel = "spring")
public interface Temi15UteQueIdMapper {
    Temi15UteQueIdDto toDto(Temi15UteQueId id);
    Temi15UteQueId toEntity(Temi15UteQueIdDto dto);
}
