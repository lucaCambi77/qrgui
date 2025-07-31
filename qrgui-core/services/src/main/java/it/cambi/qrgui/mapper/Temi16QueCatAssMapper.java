package it.cambi.qrgui.mapper;

import it.cambi.qrgui.dto.Temi16QueCatAssDto;
import it.cambi.qrgui.model.Temi16QueCatAss;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// For Temi16QueCatAss
@Mapper(componentModel = "spring", uses = {Temi16QueCatAssIdMapper.class, Temi14UteCatIdMapper.class, Temi15UteQueIdMapper.class})
public interface Temi16QueCatAssMapper {
    Temi16QueCatAssDto toDto(Temi16QueCatAss entity);
    Temi16QueCatAss toEntity(Temi16QueCatAssDto dto);
}
