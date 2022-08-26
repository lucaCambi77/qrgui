package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi17UteRouId;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITemi17Service<T> {
  WrappedResponse<List<Temi17UteRou>> findAll(HttpServletRequest request);

  WrappedResponse<T> delete(Temi17UteRouId crou);

  WrappedResponse<T> merge(T temi17);
}
