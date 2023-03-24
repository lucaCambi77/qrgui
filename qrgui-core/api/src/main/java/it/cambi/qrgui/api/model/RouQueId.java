package it.cambi.qrgui.api.model;

import java.util.Date;

public record RouQueId(long rou, long que, Date insQue, Date insRou) {
}
