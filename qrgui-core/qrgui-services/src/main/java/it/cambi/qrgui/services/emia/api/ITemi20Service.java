package it.cambi.qrgui.services.emia.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.cambi.qrgui.model.Temi20AnaTipCat;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

public interface ITemi20Service<T> {
  List<T> findAll();

  WrappedResponse<List<T>> getByCategory(HttpServletRequest request);

  List<String> getFunctionsByRequest(HttpServletRequest request);

  void merge(Temi20AnaTipCat temi20AnaTipCat);
}
