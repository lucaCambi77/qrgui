package it.cambi.qrgui.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// Temi16QueCatAssIdDto.java
@NoArgsConstructor
@Data
public class Temi16QueCatAssIdDto {
    private long que;
    private long cat;
    private Date insCat;
    private Date insQue;
    // getters/setters
}
