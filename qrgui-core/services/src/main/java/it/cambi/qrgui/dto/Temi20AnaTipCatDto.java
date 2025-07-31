package it.cambi.qrgui.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

// Temi20AnaTipCatDto.java
@Data
@NoArgsConstructor
public class Temi20AnaTipCatDto {
    private String tipCat;
    private String des;
    // optionally list of Temi14UteCatDto or just their IDs
    // getters/setters
}
