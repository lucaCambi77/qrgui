package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.dto.Temi17UteRouDto;
import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi17UteRouId;
import java.util.List;

public interface ITemi17Service<T> {
  List<Temi17UteRouDto> findAll(List<String> functions);

  void delete(Temi17UteRouId crou);

  Temi17UteRouDto merge(T temi17);
}
