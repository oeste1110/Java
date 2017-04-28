package com.potevio.dao.main;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Created by oeste on 2017/4/27.
 */
public class QueryCondition {
    private Criterion criterion;

    public QueryCondition()
    {

    }

    public QueryCondition(Criterion criterion)
    {
        this.criterion = criterion;
    }

    public Criterion getCriterion()
    {
        return criterion;
    }

    public void equal(String name,Object value)
    {
        this.criterion = Restrictions.eq(name,value);
    }

    public void less(String name,Object value,boolean eqornot)
    {
        this.criterion = eqornot?Restrictions.le(name,value):Restrictions.lt(name,value);
    }

    public void great(String name,Object value,boolean eqornot)
    {
        this.criterion = eqornot?Restrictions.ge(name,value):Restrictions.gt(name,value);
    }

    public void between(String name,Object lo,Object up)
    {
        this.criterion = Restrictions.between(name,lo,up);
    }

    public QueryCondition and(QueryCondition condition)
    {
        Criterion reCriterion = Restrictions.and(condition.getCriterion());
        return new QueryCondition(reCriterion);
    }

    public QueryCondition or(QueryCondition condition)
    {
        Criterion reCriterion = Restrictions.or(condition.getCriterion());
        return new QueryCondition(reCriterion);
    }


}
