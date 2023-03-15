package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.model.Temi16QueCatAss;

import java.util.List;

public interface ITemi16Service<T> {
  List<T> findByCategory(List<String> functions);

  Integer addQueriesToCateg(List<Temi16QueCatAss> temi16);
}
