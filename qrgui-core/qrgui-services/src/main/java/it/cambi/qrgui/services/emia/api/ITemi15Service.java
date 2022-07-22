package it.cambi.qrgui.services.emia.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

public interface ITemi15Service<T> {

  WrappedResponse<T> postQuery(T que, String locale);

  WrappedResponse<T> getByPk(Long cQue, Long dateIns);

  WrappedResponse<List<T>> getByTipCateg(
      List<String> listAllowedCat, List<Temi15UteQue> queries, HttpServletRequest request);

  WrappedResponse<List<T>> getByDb(String schema, String type);

  WrappedResponse<List<Object>> getAlreadyAssociatedQuery(int ccat, Long insCat, String tipCat);

  WrappedResponse<T> deleteQuery(Temi15UteQueId cque);
}
