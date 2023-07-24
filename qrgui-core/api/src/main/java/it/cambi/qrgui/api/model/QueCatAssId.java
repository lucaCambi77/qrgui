package it.cambi.qrgui.api.model;

import java.util.Date;
import lombok.Builder;

@Builder
public record QueCatAssId(long que, long cat, Date insCat, Date insQue) {}
