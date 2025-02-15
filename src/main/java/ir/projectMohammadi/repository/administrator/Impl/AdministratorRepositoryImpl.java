package ir.projectMohammadi.repository.administrator.Impl;

import ir.projectMohammadi.model.administor.Administrator;
import ir.projectMohammadi.repository.administrator.IAdministratorRepository;
import ir.projectMohammadi.repository.base.impl.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;

public class AdministratorRepositoryImpl extends BaseRepositoryImpl<Administrator,Long> implements IAdministratorRepository {
    @Override
    public Class<Administrator> getEntityClass() {
        return Administrator.class;
    }

    public AdministratorRepositoryImpl(EntityManager entityManager) {
        super(Administrator.class, entityManager);
    }
}
