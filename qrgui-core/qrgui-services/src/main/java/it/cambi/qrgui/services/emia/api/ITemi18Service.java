package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi18RouQueId;

import java.util.List;

public interface ITemi18Service<T> {
    T merge(Temi18RouQueId temi18Pk);

    T deleteQueRoutineAssoc(Temi18RouQueId temi18Pk);

    List<Temi18RouQue> getQueRoutineByQueryId(Temi15UteQueId cque);
}
