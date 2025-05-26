package DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Manages Hibernate sessions using a thread-local pattern.
 * This ensures that each thread of execution has its own Hibernate Session for a given unit of work.
 *
 * How to use:
 * 1. Obtain a Session: Call `DB.openSession()` at the start of a logical unit of work (e.g., a service method).
 *    This will return an existing open session if one is already associated with the current thread
 *    for the current unit of work, or it will create a new one if none exists or the previous one was closed.
 *
 * 2. Transactions (Recommended for CUD operations or atomic reads):
 *    If your unit of work involves creating, updating, or deleting entities, or requires
 *    multiple read operations to be atomic, it's best practice to manage transactions explicitly:
 *    `org.hibernate.Transaction tx = session.beginTransaction();`
 *
 * 3. Perform Operations: Use the obtained `session` object to interact with the database,
 *    typically through DAO methods or directly. DAOs should be designed to use the
 *    session provided by `DB.openSession()`.
 *
 * 4. Commit or Rollback Transaction:
 *    - If all operations within the transaction are successful: `tx.commit();`
 *    - If any operation fails or an exception occurs: `if (tx != null && tx.isActive()) { tx.rollback(); }`
 *
 * 5. Close the Session (CRITICALLY IMPORTANT):
 *    Always ensure `DB.closeSession()` is called in a `finally` block at the end of your
 *    unit of work. This action performs two vital functions:
 *    a) It closes the underlying Hibernate Session, releasing database connections and other resources.
 *    b) It removes the Session object from the current thread's local storage (`ThreadLocal`).
 *    This cleanup is essential to prevent resource leaks and to ensure that subsequent calls
 *    to `DB.openSession()` by the same thread (for a *new* unit of work) will correctly
 *    receive a fresh session if needed, rather than an old or closed one.
 *
 * 6. Application Shutdown:
 *    Call `DB.shutdown()` when your application is terminating to release the
 *    Hibernate SessionFactory and its associated resources (like connection pools).
 *
 * Example (Recommended Usage Pattern):
 * Session session = null; // Declare session outside try to be accessible in finally
 * org.hibernate.Transaction tx = null;
 * try {
 *     session = DB.openSession(); // Get or create a session for the current thread's unit of work
 *     tx = session.beginTransaction(); // Start a transaction
 *
 *     // --- Your database operations start here ---
 *     // Example:
 *     // YourEntityDAO dao = new YourEntityDAO(); // Assuming DAO uses DB.openSession() internally
 *     // YourEntity entity = new YourEntity();
 *     // dao.save(entity); // or session.persist(entity);
 *     // --- Your database operations end here ---
 *
 *     tx.commit(); // Commit the transaction if all operations succeed
 *     System.out.println("Operations completed successfully.");
 *
 * } catch (Exception e) {
 *     if (tx != null && tx.isActive()) {
 *         try {
 *             tx.rollback(); // Roll back the transaction if any error occurs
 *             System.err.println("Transaction rolled back due to an error.");
 *         } catch (Exception rbEx) {
 *             System.err.println("Error during transaction rollback: " + rbEx.getMessage());
 *         }
 *     }
 *     System.err.println("Error during database operations: " + e.getMessage());
 *     e.printStackTrace(); // Consider using a proper logging framework
 * } finally {
 *     // CRITICAL: Always close the session to release resources and clear the thread-local variable.
 *     DB.closeSession();
 *     // System.out.println("Session handling completed for the current unit of work.");
 * }
 *
 * // At application shutdown (e.g., in a servlet context listener or main method's end):
 * // DB.shutdown();
 */
public class DB {
    private static final SessionFactory sessionFactory;
    private static final ThreadLocal<Session> threadLocalSession = new ThreadLocal<>();

    static {
        try {
            // Loads hibernate.cfg.xml by default
            Configuration configuration = new Configuration().configure();
            // Recommended setting for production: "validate" or "none".
            // "validate": checks if the DB schema matches the entity mappings.
            // "none": does nothing with the schema.
            // Avoid "create", "create-drop", "update" in production unless their behavior is fully understood.
            configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
            sessionFactory = configuration.buildSessionFactory();
            System.out.println("Hibernate SessionFactory initialized successfully.");
        } catch (Throwable ex) {
            System.err.println("Initial Hibernate SessionFactory creation failed: " + ex);
            // This is a critical error, so rethrow to halt application startup if SessionFactory fails.
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Retrieves or creates a Hibernate Session for the current thread's unit of work.
     * If a session is already open and associated with the current thread (and was not previously closed
     * by `closeSession()` for this unit of work), that session is returned.
     * Otherwise, a new session is opened from the SessionFactory, associated with the current thread
     * via ThreadLocal storage, and then returned.
     *
     * This method is the primary way to obtain a session for database operations within a defined unit of work.
     *
     * @return The active Hibernate Session for the current thread.
     * @throws org.hibernate.HibernateException if an error occurs opening a new session from the factory.
     */
    public static Session openSession() {
        Session session = threadLocalSession.get();
        if (session == null || !session.isOpen()) {
            // No session currently associated with this thread for a unit of work,
            // or the previously associated session was closed (e.g., by a previous call to closeSession()).
            // Open a new session from the factory.
            session = sessionFactory.openSession();
            threadLocalSession.set(session); // Store the new session in ThreadLocal for this thread.
            // System.out.println("New Hibernate session opened and set for thread: " + Thread.currentThread().getName());
        } else {
            // System.out.println("Reusing existing Hibernate session for thread: " + Thread.currentThread().getName());
        }
        return session;
    }

    /**
     * Closes the Hibernate Session currently associated with the calling thread for its unit of work.
     * This method performs two key actions:
     * 1. If the session exists and is open, it attempts to close the Hibernate Session.
     * 2. It *always* removes the session reference from the thread-local storage for the current thread.
     *
     * This removal from `ThreadLocal` is crucial to prevent resource leaks (especially in environments
     * with thread pools) and to ensure that a subsequent call to `openSession()` by this thread
     * (for a new unit of work) will correctly obtain a fresh session if the old one was indeed closed.
     *
     * This method MUST be called in a `finally` block at the end of each logical unit of work
     * where `openSession()` was used. It is safe to call this method even if no session
     * is currently open or associated with the thread, or if the session was already closed.
     */
    public static void closeSession() {
        Session session = threadLocalSession.get();
        if (session != null && session.isOpen()) {
            try {
                session.close();
                // System.out.println("Hibernate session closed for thread: " + Thread.currentThread().getName());
            } catch (org.hibernate.HibernateException e) {
                System.err.println("Error closing Hibernate session for thread " + Thread.currentThread().getName() + ": " + e.getMessage());
                // Log this error, but still proceed to remove from ThreadLocal.
            }
        }
        // Always remove the session from ThreadLocal, regardless of its state or if it was null.
        // This ensures that the thread does not hold a stale reference.
        threadLocalSession.remove();
        // System.out.println("ThreadLocal session reference removed for thread: " + Thread.currentThread().getName());
    }

    /**
     * Shuts down the Hibernate SessionFactory.
     * This method should be called when the application is terminating (e.g., in a
     * `ServletContextListener.contextDestroyed` method for web applications, or at the
     * end of a standalone application's main method). It releases all database connection
     * pools and other resources managed by the SessionFactory.
     * After this method is called, no new sessions can be created via `openSession()`.
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            try {
                sessionFactory.close();
                System.out.println("Hibernate SessionFactory shut down successfully.");
            } catch (org.hibernate.HibernateException e) {
                System.err.println("Error shutting down Hibernate SessionFactory: " + e.getMessage());
            }
        } else if (sessionFactory != null && sessionFactory.isClosed()) {
            System.out.println("Hibernate SessionFactory was already closed.");
        } else {
            // This case should ideally not be reached if static initializer succeeded.
            System.out.println("Hibernate SessionFactory was not initialized or already null before shutdown attempt.");
        }
    }
}