package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.model.Temi20AnaTipCat;

import java.util.List;

public interface ITemi20Service<T> {
  List<T> findAll();

  List<T> getByCategory(List<String> functions);

  void merge(Temi20AnaTipCat temi20AnaTipCat);
}
