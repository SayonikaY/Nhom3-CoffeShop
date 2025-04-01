package model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


/**
 * Database class to handle database operations
 *         Session session = DB.openSession();
 *         // Do your shit
 *         DB.closeSession();
 *
 * How to use: Before doing ANYTHING, call openSession() to open a new session
 * Then, do whatever you need to do like query, save, update, delete, etc.
 * Finally, call closeSession() to close the current session
 * And call shutdown() when close the program (exit button or sth you figure out yourself bruh)
 *
 * Example:
 *  Query: banList = session.createQuery("FROM Ban", Ban.class).list();
 *  Save: Database.saveAndClose(ban)
 *  Update, delete:
 *      tx = session.beginTransaction();
 *      session.merge(product);
 *      session.remove(product);
 *      tx.commit();
 */

public class DB {
    private static final SessionFactory sessionFactory;
    private static ThreadLocal<Session> threadLocalSession = new ThreadLocal<>();

    static {
        try {
            Configuration configuration = new Configuration().configure();
            // Change schema strategy to validate to avoid modification attempts
            configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Opens a new session and stores it in ThreadLocal
     */
    public static Session openSession() {
        Session session = sessionFactory.openSession();
        threadLocalSession.set(session);
        return session;
    }

    /**
     * Saves an entity to the database and closes the session
     * @param entity The entity to save
     * @return true if save was successful, false otherwise
     */
    public static boolean saveAndClose(Object entity) {
        Session session = threadLocalSession.get();
        if (session == null) {
            session = openSession();
        }

        Transaction tx = null;
        boolean success = false;

        try {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            success = true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            closeSession();
        }

        return success;
    }

    /**
     * Closes the current session
     */
    public static void closeSession() {
        Session session = threadLocalSession.get();
        if (session != null) {
            session.close();
            threadLocalSession.remove();
        }
    }

    /**
     * Shutdown method to be called when application is terminating
     */
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
