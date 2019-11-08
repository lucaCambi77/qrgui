package it.cambi.qrgui.services;

import java.util.List;

import it.cambi.qrgui.services.util.wrappedResponse.WrappedResponse;

public interface ITemi13Service<T>
{

    WrappedResponse<List<T>> findAll();

}
