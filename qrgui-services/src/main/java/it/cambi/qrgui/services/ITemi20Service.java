package it.cambi.qrgui.services;

import it.cambi.qrgui.services.util.wrappedResponse.WrappedResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITemi20Service<T>
{

    List<T> findAll();

    WrappedResponse<List<T>> getByCategory(HttpServletRequest request);

    List<String> getFunctionsByRequest(HttpServletRequest request);

}
