package it.cambi.qrgui.dao.entity.api;

import it.cambi.qrgui.dao.api.IEntityDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITemi20Dao<T, K> extends IEntityDao<T, K>
{
    List<T> findByAllowedCategories(List<String> functions);

    List<String> getFunctionsByRequest(HttpServletRequest request);

}
