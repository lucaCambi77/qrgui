package it.cambi.qrgui.services.emia.api;

import java.util.List;

import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

public interface ITemi13Service<T>
{

    WrappedResponse<List<T>> findAll();

}
