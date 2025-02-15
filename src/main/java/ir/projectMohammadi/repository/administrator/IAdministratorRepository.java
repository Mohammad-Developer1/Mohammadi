package ir.projectMohammadi.repository.administrator;

import ir.projectMohammadi.model.administor.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdministratorRepository extends JpaRepository<Administrator, Long> {

}
