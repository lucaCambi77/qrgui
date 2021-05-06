package it.cambi.qrgui.dao.entity.api;

import it.cambi.qrgui.dao.api.IEntityDao;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITemi20Dao<T, K> extends IEntityDao<T, K>
{

    WrappedResponse<List<T>> findByAllowedCategories(HttpServletRequest request);

    List<String> getFunctionsByRequest(HttpServletRequest request);

}
