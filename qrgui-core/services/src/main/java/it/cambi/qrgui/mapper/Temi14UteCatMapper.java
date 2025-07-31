package it.cambi.qrgui.mapper;

import it.cambi.qrgui.dto.Temi14UteCatDto;
import it.cambi.qrgui.model.Temi14UteCat;
import java.util.List;
import org.mapstruct.Mapper;

// For Temi14UteCat
@Mapper(
    componentModel = "spring",
    uses = {Temi20AnaTipCatMapper.class, Temi16QueCatAssMapper.class})
public interface Temi14UteCatMapper {
  Temi14UteCatDto toDto(Temi14UteCat entity);

  Temi14UteCat toEntity(Temi14UteCatDto dto);

  List<Temi14UteCatDto> toDtoList(List<Temi14UteCat> entities);

  List<Temi14UteCat> toEntityList(List<Temi14UteCatDto> dtos);
}
