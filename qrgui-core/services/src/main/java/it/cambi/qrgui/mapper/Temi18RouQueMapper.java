package it.cambi.qrgui.mapper;

import it.cambi.qrgui.dto.Temi18RouQueDto;
import it.cambi.qrgui.model.Temi18RouQue;
import org.mapstruct.Mapper;

// For Temi18RouQue
@Mapper(
    componentModel = "spring",
    uses = {Temi18RouQueIdMapper.class, Temi15UteQueIdMapper.class})
public interface Temi18RouQueMapper {
  Temi18RouQueDto toDto(Temi18RouQue entity);

  Temi18RouQue toEntity(Temi18RouQueDto dto);
}
