package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi17UteRouId;
import java.util.List;

public interface ITemi17Service<T> {
  List<Temi17UteRou> findAll(List<String> functions);

  void delete(Temi17UteRouId crou);

  T merge(T temi17);
}
