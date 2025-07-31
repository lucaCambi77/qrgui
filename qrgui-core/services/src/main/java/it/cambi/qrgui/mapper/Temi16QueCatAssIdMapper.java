package it.cambi.qrgui.mapper;

import it.cambi.qrgui.dto.Temi16QueCatAssIdDto;
import it.cambi.qrgui.model.Temi16QueCatAssId;
import org.mapstruct.Mapper;

// For Temi16QueCatAssId
@Mapper(componentModel = "spring")
public interface Temi16QueCatAssIdMapper {
    Temi16QueCatAssIdDto toDto(Temi16QueCatAssId id);
    Temi16QueCatAssId toEntity(Temi16QueCatAssIdDto dto);
}
