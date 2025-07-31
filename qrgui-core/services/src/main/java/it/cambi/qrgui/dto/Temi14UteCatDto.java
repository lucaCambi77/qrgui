package it.cambi.qrgui.dto;

import java.util.Date;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Temi14UteCatDto {
  private int cat;
  private Date insCat;
  private Temi20AnaTipCatDto temi20AnaTipCat;
  private Integer par;
  private Date insPar;
  private String des;
  private Set<Temi16QueCatAssDto> Temi16QueCatAsses;

  // optionally, you can add a list of Temi16QueCatAssDto or their IDs
  // getters/setters
}
