package uz.zako.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.zako.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID > {

    public Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByIdAndActive(UUID id, Boolean active);
}
