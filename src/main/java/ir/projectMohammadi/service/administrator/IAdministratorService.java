package ir.projectMohammadi.service.administrator;

import ir.projectMohammadi.model.administor.Administrator;

import java.util.List;

public interface IAdministratorService {
    Boolean saveAndUpdateAdministrator(Administrator administrator);
    List<Administrator> getAllAdministrator();
    Administrator getAdministrator(Long id);
    Boolean deleteAdministrator(Long id);
}
