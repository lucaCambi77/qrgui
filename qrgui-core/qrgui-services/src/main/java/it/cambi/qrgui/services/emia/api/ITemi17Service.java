package it.cambi.qrgui.services.emia.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi17UteRouId;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

public interface ITemi17Service<T> {
  WrappedResponse<List<Temi17UteRou>> findAll(HttpServletRequest request);

  WrappedResponse<T> delete(Temi17UteRouId crou);

  WrappedResponse<T> merge(T temi17);
}
