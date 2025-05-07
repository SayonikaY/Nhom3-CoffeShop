package DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
 try {
    session = DB.openSession(); // Get the session
    tx = session.beginTransaction(); // Start a transaction

        banDAO.save(...);
        somethingelseDAO.save(..);
        somethingelseDAO.delete(..);
        nói chung là your code

    tx.commit(); // Commit the transaction if all operations succeed

    } catch (Exception e) {
        if (tx != null) {
        tx.rollback(); // Roll back the transaction if any error occurs
        }
    // Handle or log the exception
    } finally {
        if (session != null) {
            DB.closeSession(); // Close the session in a finally block
        }
    }
 }
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
