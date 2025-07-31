package it.cambi.qrgui.mapper;

import it.cambi.qrgui.dto.Temi20AnaTipCatDto;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// For Temi20AnaTipCat
@Mapper(componentModel = "spring")
public interface Temi20AnaTipCatMapper {
  Temi20AnaTipCatDto toDto(Temi20AnaTipCat entity);

  @Mapping(target = "temi14UteCats", ignore = true)
  Temi20AnaTipCat toEntity(Temi20AnaTipCatDto dto);

  List<Temi20AnaTipCatDto> toDtoList(List<Temi20AnaTipCat> entities);

  List<Temi20AnaTipCat> toEntityList(List<Temi20AnaTipCatDto> dtos);
}
