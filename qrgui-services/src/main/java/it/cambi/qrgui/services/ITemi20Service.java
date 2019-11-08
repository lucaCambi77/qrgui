package it.cambi.qrgui.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.cambi.qrgui.services.util.wrappedResponse.WrappedResponse;

public interface ITemi20Service<T>
{

    List<T> findAll();

    WrappedResponse<List<T>> getByCategory(HttpServletRequest request);

    List<String> getFunctionsByRequest(HttpServletRequest request);

}
