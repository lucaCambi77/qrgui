package it.cambi.qrgui.dao.entity.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.cambi.qrgui.dao.api.IEntityDao;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

public interface ITemi20Dao<T, K> extends IEntityDao<T, K>
{

    WrappedResponse<List<T>> findByAllowedCategories(HttpServletRequest request);

    List<String> getFunctionsByRequest(HttpServletRequest request);

}
