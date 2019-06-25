/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Aluguel;
import domain.Cliente;
import domain.state.aluguel.EstadoAluguel;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author gudeck
 */
public class DAOAluguel extends GenericDAO {

    private static DAOAluguel UNIQUEINSTANCE;

    private DAOAluguel() {
    }

    public static synchronized DAOAluguel getInstance() {
        if (UNIQUEINSTANCE == null) {
            UNIQUEINSTANCE = new DAOAluguel();
        }

        return UNIQUEINSTANCE;
    }

    public List<Aluguel> readEstadoIndireto(Cliente cliente, EstadoAluguel estado) {
        List lista = null;
        Session sessao = null;

        try {
            // Abrir a SESSÃO
            sessao = ConexaoHibernate.getSESSIONFACTORY().openSession();
            sessao.getTransaction().begin();

            CriteriaBuilder builder = sessao.getCriteriaBuilder();

            CriteriaQuery<Aluguel> criteria = builder.createQuery(Aluguel.class);
            Root<Aluguel> root = criteria.from(Aluguel.class);

            criteria.where(builder.and(
                    builder.equal(root.get("cliente"), cliente)),
                    builder.not(builder.equal(root.get("estado"), estado)
                    ));

            lista = sessao.createQuery(criteria).getResultList();

            sessao.getTransaction().commit();
            sessao.close();
        } catch (HibernateException ex) {
            if (sessao != null) {
                sessao.getTransaction().rollback();
                sessao.close();
            }

            throw new HibernateException(ex);
        }
        return lista;
    }

    public Aluguel readEstadoDireto(Cliente cliente, EstadoAluguel estado) {
        List lista = null;
        Session sessao = null;

        try {
            // Abrir a SESSÃO
            sessao = ConexaoHibernate.getSESSIONFACTORY().openSession();
            sessao.getTransaction().begin();

            CriteriaBuilder builder = sessao.getCriteriaBuilder();

            CriteriaQuery<Aluguel> criteria = builder.createQuery(Aluguel.class);
            Root<Aluguel> root = criteria.from(Aluguel.class);

            criteria.where(builder.and(
                    builder.equal(root.get("estado"), estado),
                    builder.equal(root.get("cliente"), cliente)
            ));

            lista = sessao.createQuery(criteria).getResultList();

            sessao.getTransaction().commit();
            sessao.close();
        } catch (HibernateException ex) {
            if (sessao != null) {
                sessao.getTransaction().rollback();
                sessao.close();
            }

            throw new HibernateException(ex);
        }
        if (lista.size() > 0) {
            return (Aluguel) lista.get(0);
        } else {
            return null;
        }
    }

}
