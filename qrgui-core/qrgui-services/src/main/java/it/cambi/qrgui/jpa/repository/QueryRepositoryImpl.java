/**
 * 
 */
package it.cambi.qrgui.jpa.repository;

import it.cambi.qrgui.services.db.model.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luca
 *
 */
@Repository
public class QueryRepositoryImpl implements QueryRepository
{

    @PersistenceContext(unitName = "emiaPU")
    @Qualifier(value = "emiaEntityManagerFactory")
    private EntityManager em;

    public CriteriaBuilder getCriteriaBuilder()
    {
        return em.getCriteriaBuilder();
    }

    protected EntityManager getEntityManager()
    {
        return em;
    }

    public List<Temi15UteQue> getByDb(String schema, String type)
    {
        CriteriaQuery<Temi15UteQue> criteriaQuery = getCriteriaBuilder().createQuery(Temi15UteQue.class);
        Root<Temi15UteQue> root = criteriaQuery.from(Temi15UteQue.class);

        Expression<?> dbExpr = root.get(Temi15UteQue_.temi13DtbInf).get(Temi13DtbInf_.id);
        Temi13DtbInfId pk = new Temi13DtbInfId();
        pk.setSch(schema);
        pk.setTyp(type);

        List<Predicate> predicateList = new ArrayList<>();

        Predicate predicate1 = getEntityManager().getCriteriaBuilder().equal(dbExpr, pk);

        /* caso AND */
        predicateList.add(predicate1);
        Predicate[] finalPredicateList = new Predicate[predicateList.size()];
        predicateList.toArray(finalPredicateList);
        criteriaQuery.where(finalPredicateList);
        criteriaQuery.orderBy(getCriteriaBuilder().asc(root.get(Temi15UteQue_.id).get(Temi15UteQueId_.que)));

        return getEntityManager().createQuery(criteriaQuery)
                .getResultList();

    }
}
