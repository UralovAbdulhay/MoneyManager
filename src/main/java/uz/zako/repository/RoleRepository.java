package uz.zako.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.zako.entity.Role;

import java.util.List;
import java.util.UUID;


public interface RoleRepository extends JpaRepository<Role, UUID> {

    List<Role> findAllByName(String role_user);

    boolean existsByName(String name);


}
