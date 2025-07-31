package it.cambi.qrgui.dto;

import java.util.Date;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Temi17UteRouDto {
  private long rou;
  private Date insRou;
  private String des;
  private Set<Temi18RouQueDto> temi18RouQues;
  // getters/setters
}
