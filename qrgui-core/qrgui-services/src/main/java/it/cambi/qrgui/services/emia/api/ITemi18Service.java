package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.model.Temi18RouQueId;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

public interface ITemi18Service<T>
{

    WrappedResponse<T> merge(Temi18RouQueId temi18Pk);

    WrappedResponse<T> deleteQueRoutineAssoc(Temi18RouQueId temi18Pk);

}
