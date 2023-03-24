package it.cambi.qrgui.api.model;

import java.util.Date;
import java.util.Set;

public record UteRouDto(String des, Set<RouQueDto> rouQues, long rou, Date insRou) {
}
