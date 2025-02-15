package ir.projectMohammadi.repository.base.impl;

import ir.projectMohammadi.repository.base.IBaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public abstract class BaseRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements IBaseRepository<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    public abstract Class<T> getEntityClass();

    @Override
    @Transactional
    public T saveAndUpdate(T t) {
        if (t == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if (entityManager.contains(t)) {
            entityManager.persist(t);
        } else {
            entityManager.merge(t);
        }
        return t;
    }

    @Override
    @Transactional
    public Boolean deleteByID(ID id) {
        T entity = findByID(id);
        if (entity != null) {
            entityManager.remove(entity);
            return true;
        }
        return false;
    }

    @Override
    public List<T> findAll() {
        return entityManager.createQuery("SELECT e FROM " + getEntityClass().getSimpleName() + " e", getEntityClass())
                .getResultList();
    }

    @Override
    public T findByID(ID id) {
        return entityManager.find(getEntityClass(), id);
    }
}
