package DAO;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Transaction;

public abstract class BaseDAO<T, ID> {
    private Class<T> entityClass;

    protected BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Session getCurrentSession() {
        return DB.openSession();
    }

    public void save(T entity) {
        getCurrentSession().persist(entity);
    }

    public void update(T entity) {
        getCurrentSession().merge(entity);
    }

    public void delete(T entity) {
        getCurrentSession().remove(entity);
    }

    public T findById(ID id) {
        return getCurrentSession().get(entityClass, id);
    }

    /**
     * Get all entities of type T
     * @return List of all entities
     */
    public List<T> findAll() {
        String queryString = "FROM " + entityClass.getName();
        Query<T> query = getCurrentSession().createQuery(queryString, entityClass);
        return query.list();
    }

    /**
     * Find entities by a specific field
     * @param fieldName Field name to search by
     * @param value Value to match
     * @return List of matching entities
     */
    public List<T> findByField(String fieldName, Object value) {
        String queryString = "FROM " + entityClass.getName() + " WHERE " + fieldName + " = :value";
        Query<T> query = getCurrentSession().createQuery(queryString, entityClass);
        query.setParameter("value", value);
        return query.list();
    }

    /**
     * Find first entity matching a field value
     * @param fieldName Field name to search by
     * @param value Value to match
     * @return Optional containing entity if found
     */
    public Optional<T> findFirstByField(String fieldName, Object value) {
        String queryString = "FROM " + entityClass.getName() + " WHERE " + fieldName + " = :value";
        Query<T> query = getCurrentSession().createQuery(queryString, entityClass);
        query.setParameter("value", value);
        query.setMaxResults(1);
        return query.uniqueResultOptional();
    }

    /**
     * Search entities where field contains the given value
     * @param fieldName Field name to search in
     * @param searchTerm Value to search for (will be wrapped with % automatically)
     * @return List of matching entities
     */
    public List<T> searchByFieldContains(String fieldName, String searchTerm) {
        String queryString = "FROM " + entityClass.getName() +
                " WHERE " + fieldName + " LIKE :value";
        Query<T> query = getCurrentSession().createQuery(queryString, entityClass);
        query.setParameter("value", "%" + searchTerm + "%");
        return query.list();
    }

    /**
     * Search with multiple criteria using LIKE for String values
     * @param criteria Map of field names to values
     * @param likeFields Set of field names that should use LIKE operator
     * @return List of matching entities
     */
    public List<T> searchByCriteria(Map<String, Object> criteria, List<String> likeFields) {
        if (criteria.isEmpty()) {
            return findAll();
        }

        StringBuilder queryBuilder = new StringBuilder("FROM " + entityClass.getName() + " WHERE ");
        boolean first = true;

        for (String fieldName : criteria.keySet()) {
            if (!first) {
                queryBuilder.append(" AND ");
            }

            if (likeFields.contains(fieldName) && criteria.get(fieldName) instanceof String) {
                queryBuilder.append(fieldName).append(" LIKE :").append(fieldName);
            } else {
                queryBuilder.append(fieldName).append(" = :").append(fieldName);
            }
            first = false;
        }

        Query<T> query = getCurrentSession().createQuery(queryBuilder.toString(), entityClass);

        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (likeFields.contains(key) && value instanceof String) {
                query.setParameter(key, "%" + value + "%");
            } else {
                query.setParameter(key, value);
            }
        }

        return query.list();
    }

    /**
     * Count all entities of type T
     * @return count of entities
     */
    public long count() {
        String queryString = "SELECT COUNT(*) FROM " + entityClass.getName();
        Query<Long> query = getCurrentSession().createQuery(queryString, Long.class);
        return query.uniqueResult();
    }

    /**
     * Check if entity with given id exists
     * @param id Entity id
     * @return true if entity exists, false otherwise
     */
    public boolean existsById(ID id) {
        return findById(id) != null;
    }

    /**
     * Delete entity by id
     * @param id Entity id
     */
    public void deleteById(ID id) {
        T entity = findById(id);
        if (entity != null) {
            delete(entity);
        }
    }
}