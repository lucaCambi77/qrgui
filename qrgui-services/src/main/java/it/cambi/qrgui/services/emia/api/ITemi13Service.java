package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.services.util.wrappedResponse.WrappedResponse;

import java.util.List;

public interface ITemi13Service<T>
{

    WrappedResponse<List<T>> findAll();

}
