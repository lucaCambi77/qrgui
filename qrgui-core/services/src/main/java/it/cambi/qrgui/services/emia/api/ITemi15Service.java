package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi15UteQueId;
import java.text.ParseException;
import java.util.List;

public interface ITemi15Service<T> {

  T postQuery(UteQueDto que, String locale);

  List<T>
  getByTipCateg(
      List<String> listAllowedCat, List<Temi15UteQue> queries, List<String> functions);

  List<Object> getAlreadyAssociatedQuery(int ccat, String insCat) throws ParseException;

  T deleteQuery(Temi15UteQueId cque);
}
