package it.cambi.qrgui.api.model;

import lombok.Builder;

import java.util.Date;

@Builder
public record QueCatAssId(long que, long cat, Date insCat, Date insQue) {
}
