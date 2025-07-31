package it.cambi.qrgui.mapper;

import it.cambi.qrgui.dto.Temi17UteRouDto;
import it.cambi.qrgui.model.Temi17UteRou;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// For Temi17UteRou
@Mapper(componentModel = "spring")
public interface Temi17UteRouMapper {
  Temi17UteRouDto toDto(Temi17UteRou entity);

  @Mapping(target = "temi18RouQues", ignore = true)
  Temi17UteRou toEntity(Temi17UteRouDto dto);

  List<Temi17UteRouDto> toDtoList(List<Temi17UteRou> entities);

  List<Temi17UteRou> toEntityList(List<Temi17UteRouDto> dtos);
}
