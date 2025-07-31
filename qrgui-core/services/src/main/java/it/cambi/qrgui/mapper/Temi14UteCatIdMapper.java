package it.cambi.qrgui.mapper;

import it.cambi.qrgui.dto.Temi14UteCatIdDto;
import it.cambi.qrgui.model.Temi14UteCatId;
import org.mapstruct.*;

// For Temi14UteCatId
@Mapper(componentModel = "spring")
public interface Temi14UteCatIdMapper {
    Temi14UteCatIdDto toDto(Temi14UteCatId id);
    Temi14UteCatId toEntity(Temi14UteCatIdDto dto);
}

