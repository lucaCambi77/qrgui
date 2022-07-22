package it.cambi.qrgui.dao.entity.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.cambi.qrgui.dao.api.IEntityDao;
import it.cambi.qrgui.model.Temi16QueCatAss;

public interface ITemi16Dao<T, K> extends IEntityDao<T, K> {

  List<T> findByCategory(HttpServletRequest request);

  Integer addQueriesToCateg(List<Temi16QueCatAss> temi16);
}
