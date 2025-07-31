package it.cambi.qrgui.mapper;

import it.cambi.qrgui.dto.Temi15UteQueDto;
import it.cambi.qrgui.model.Temi15UteQue;
import org.mapstruct.Mapper;

// For Temi15UteQue
@Mapper(
    componentModel = "spring",
    uses = {Temi16QueCatAssMapper.class, Temi18RouQueMapper.class})
public interface Temi15UteQueMapper {
  Temi15UteQueDto toDto(Temi15UteQue entity);
  Temi15UteQue toEntity(Temi15UteQueDto dto);
}
