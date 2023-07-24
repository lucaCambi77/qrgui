package it.cambi.qrgui.api.model;

import java.util.Date;
import java.util.Set;
import lombok.Builder;

@Builder
public record CategoryDto(
    TipCatDto temi20AnaTipCat,
    Integer par,
    Date insPar,
    String des,
    Set<QueCatAssDto> queCatAsses,
    int cat,
    Date insCat) {}
