package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.services.db.model.Temi18RouQueId;
import it.cambi.qrgui.services.util.wrappedResponse.WrappedResponse;

public interface ITemi18Service<T>
{

    WrappedResponse<T> merge(Temi18RouQueId temi18Pk);

    WrappedResponse<T> deleteQueRoutineAssoc(Temi18RouQueId temi18Pk);

}
