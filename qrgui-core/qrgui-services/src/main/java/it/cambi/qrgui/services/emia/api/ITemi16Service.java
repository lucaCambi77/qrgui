package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITemi16Service<T>
{

    WrappedResponse<List<T>> findByCategory(HttpServletRequest request);

    WrappedResponse<Integer> addQueriesToCateg(List<Temi16QueCatAss> temi16);

}
