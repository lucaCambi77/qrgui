package it.cambi.qrgui.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// Temi18RouQueIdDto.java
@NoArgsConstructor
@Data
public class Temi18RouQueIdDto {
    private long rou;
    private long que;
    private Date insQue;
    private Date insRou;
    // getters/setters
}
