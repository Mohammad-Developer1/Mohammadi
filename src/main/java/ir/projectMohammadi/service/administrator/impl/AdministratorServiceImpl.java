package ir.projectMohammadi.service.administrator.impl;

import ir.projectMohammadi.model.administor.Administrator;
import ir.projectMohammadi.repository.administrator.IAdministratorRepository;
import ir.projectMohammadi.service.administrator.IAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorServiceImpl implements IAdministratorService {

    @Autowired
    private IAdministratorRepository administratorRepository;

    @Override
    public Boolean saveAndUpdateAdministrator(Administrator administrator) {
        if (administrator == null) {
            throw new NullPointerException("administrator is Null");
        }else {
            administratorRepository.save(administrator);
            return true;
        }
    }

    @Override
    public List<Administrator> getAllAdministrator() {
        return administratorRepository.findAll();
    }

    @Override
    public Administrator getAdministrator(Long id) {
        if (id == null) {
            throw new NullPointerException("administrator id is Null");
        }else {
            return administratorRepository.findById(id).get();
        }
    }

    @Override
    public void deleteAdministrator(Long id) {
        if (id == null) {
            throw new NullPointerException("administrator id is Null");
        }else {
            administratorRepository.deleteById(id);
        }
    }
}
