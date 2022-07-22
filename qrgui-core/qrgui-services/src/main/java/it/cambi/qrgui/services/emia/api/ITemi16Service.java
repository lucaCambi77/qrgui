package it.cambi.qrgui.services.emia.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

public interface ITemi16Service<T> {
  WrappedResponse<List<T>> findByCategory(HttpServletRequest request);

  WrappedResponse<Integer> addQueriesToCateg(List<Temi16QueCatAss> temi16);
}
