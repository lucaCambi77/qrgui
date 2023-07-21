/**
 *
 */
package it.cambi.qrgui.services.emia.impl;

import it.cambi.qrgui.dao.entity.api.ITemi20Dao;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import it.cambi.qrgui.services.emia.api.ITemi20Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luca
 */
@Component
@RequiredArgsConstructor
public class Temi20Service implements ITemi20Service<Temi20AnaTipCat> {
    private final ITemi20Dao<Temi20AnaTipCat, String> anaTipCatDao;

    @Override
    public List<Temi20AnaTipCat> findAll() {
        return anaTipCatDao.findAll(null);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Temi20AnaTipCat> getByCategory(List<String> functions) {
        return anaTipCatDao.findByAllowedCategories(functions);
    }

    @Override
    @Transactional
    public void merge(Temi20AnaTipCat temi20AnaTipCat) {
        anaTipCatDao.merge(temi20AnaTipCat);
    }
}
