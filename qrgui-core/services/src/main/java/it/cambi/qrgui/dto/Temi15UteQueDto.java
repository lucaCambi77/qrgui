package it.cambi.qrgui.dto;

import java.util.Date;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

// Temi15UteQueDto.java
@Data
@NoArgsConstructor
public class Temi15UteQueDto {
  private long que;
  private Date insQue;
  private String tenant;
  private String nam;
  private String json;
  private Set<Temi16QueCatAssDto> Temi16QueCatAsses;
  private Set<Temi18RouQueDto> Temi18RouQues;
}
