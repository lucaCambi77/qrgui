package it.cambi.qrgui.dao.entity.api;

import it.cambi.qrgui.dao.api.IEntityDao;
import it.cambi.qrgui.services.db.model.Temi16QueCatAss;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITemi16Dao<T, K> extends IEntityDao<T, K>
{

    WrappedResponse<List<T>> findByCategory(HttpServletRequest request);

    WrappedResponse<Integer> addQueriesToCateg(List<Temi16QueCatAss> temi16);

}
