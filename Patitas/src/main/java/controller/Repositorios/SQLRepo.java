package controller.Repositorios;

import db.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.util.List;

public class SQLRepo<T> implements GenericRepo<T> {
    protected String dbName;

    public void persist(T data) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.persist(data);
        t.commit();
        s.close();
    }

    public void executeQuery(String query) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.createQuery(query);
        t.commit();
        s.close();
    }

    public Object executeQuerySingleResult(String query) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        try {
            Object ret = s.createQuery(query).getSingleResult();
            t.commit();
            s.close();
            return ret;
        } catch (NoResultException e) {
            s.close();
            return null;
        }
    }

    public List<T> executeQueryListResult(String query, Class<T> c) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        try {
            List<T> ret = s.createQuery(query, c).getResultList();
            t.commit();
            s.close();
            return ret;
        } catch (NoResultException e) {
            s.close();
            return null;
        }
    }

    // Es igual a persist en este caso, pero para diferenciar la operacion
    public void update(T data) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.merge(data);
        t.commit();
        s.close();
    }

    public void remove(T obj) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.remove(obj);
        t.commit();
        s.close();
    }

    public void remove(List<T> objs) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        objs.forEach(s::remove);
        t.commit();
        s.close();
    }
}
