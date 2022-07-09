package it.cambi.qrgui.dao.entity.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.cambi.qrgui.dao.api.IEntityDao;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

public interface ITemi16Dao<T, K> extends IEntityDao<T, K>
{

    WrappedResponse<List<T>> findByCategory(HttpServletRequest request);

    WrappedResponse<Integer> addQueriesToCateg(List<Temi16QueCatAss> temi16);

}
