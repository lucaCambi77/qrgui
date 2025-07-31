package it.cambi.qrgui.mapper;

import it.cambi.qrgui.dto.Temi17UteRouIdDto;
import it.cambi.qrgui.model.Temi17UteRouId;
import org.mapstruct.Mapper;

// For Temi17UteRouId
@Mapper(componentModel = "spring")
public interface Temi17UteRouIdMapper {
    Temi17UteRouIdDto toDto(Temi17UteRouId id);
    Temi17UteRouId toEntity(Temi17UteRouIdDto dto);
}
