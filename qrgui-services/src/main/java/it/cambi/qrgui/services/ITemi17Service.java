package it.cambi.qrgui.services;

import it.cambi.qrgui.services.db.model.Temi17UteRou;
import it.cambi.qrgui.services.db.model.Temi17UteRouId;
import it.cambi.qrgui.services.util.wrappedResponse.WrappedResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITemi17Service<T>
{

    WrappedResponse<List<Temi17UteRou>> findAll(HttpServletRequest request);

    WrappedResponse<T> delete(Temi17UteRouId crou);

    WrappedResponse<T> merge(T temi17);

}
