package services.account_service;

import org.hibernate.SessionFactory;
import utils.HibernateUtil;

/**
 * Created by Ivan on 04.04.2014 in 21:14.
 */
public class UserDataSetDAOTest {
    private final SessionFactory sessionFactory = new HibernateUtil("hibernateH2.cfg.xml").getSessionFactory();

}
