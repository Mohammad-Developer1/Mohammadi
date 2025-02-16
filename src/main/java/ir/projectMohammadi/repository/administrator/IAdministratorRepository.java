package ir.projectMohammadi.repository.administrator;

import ir.projectMohammadi.model.administor.Administrator;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdministratorRepository extends JpaRepository<Administrator, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Administrator a WHERE a.ID = :id")
    int  deleteAdministratorById(@Param("id") Long id);

    default Boolean deleteAdministrator(Long id) {
        return deleteAdministratorById(id) > 0;
    }
}
