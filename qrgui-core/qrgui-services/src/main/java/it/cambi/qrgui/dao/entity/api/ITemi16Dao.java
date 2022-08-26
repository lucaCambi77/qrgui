package it.cambi.qrgui.dao.entity.api;

import it.cambi.qrgui.dao.api.IEntityDao;
import it.cambi.qrgui.model.Temi16QueCatAss;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITemi16Dao<T, K> extends IEntityDao<T, K> {

  List<T> findByCategory(HttpServletRequest request);

  Integer addQueriesToCateg(List<Temi16QueCatAss> temi16);
}
